package kg.news.service.impl;

import jakarta.annotation.Resource;
import kg.news.constant.UserConstant;
import kg.news.dto.HistoryQueryDTO;
import kg.news.entity.*;
import kg.news.repository.*;
import kg.news.result.PageResult;
import kg.news.service.HistoryService;
import kg.news.service.NewsService;
import kg.news.service.NewsTagService;
import kg.news.service.UserService;
import kg.news.utils.KeyWordUtil;
import kg.news.vo.HistoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class HistoryServiceImpl implements HistoryService {
    @Resource
    private HistoryRepository historyRepository;
    @Resource
    private NewsService newsService;
    @Resource
    private UserService userService;
    @Resource
    private NewsTagService newsTagService;
    @Resource
    private UserInterestRepository userInterestRepository;
    @Resource
    private NewsKeyWordRepository newsKeyWordRepository;
    @Resource
    private NewsRepository newsRepository;
    @Resource
    private NewsTagRepository newsTagRepository;
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public PageResult<HistoryVO> queryHistory(HistoryQueryDTO historyQueryDTO) {
        int page = historyQueryDTO.getPageNum();
        int pageSize = historyQueryDTO.getPageSize();
        if (page <= 0 || pageSize <= 0) {
            // 页码和每页大小必须大于0，未指定则默认为1和10
            page = 1;
            pageSize = 10;
        }
        // JPA分页查询中，页码从0开始
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<History> histories = historyRepository.findByUserId(historyQueryDTO.getUserId(), pageRequest);
        List<HistoryVO> historyVOList = histories.stream().map(history -> {
            // 通过新闻ID查询新闻详情信息
            News news = newsService.queryNews(history.getNewsId());
            NewsTag newsTag = newsTagService.queryNewsTag(news.getTagId());
            // 通过ID查询媒体信息
            User media = userService.queryUserById(news.getCreateUser());

            return HistoryVO.builder()
                    .id(history.getId())
                    .userId(history.getUserId())
                    .mediaId(media.getId())
                    .mediaName(media.getNickname())
                    .tagId(news.getTagId())
                    .tagName(newsTag.getName())
                    .newsId(news.getId())
                    .title(news.getTitle())
                    .cover(news.getCover())
                    .updateTime(history.getUpdateTime())
                    .deleted(news.getDeleteFlag())
                    .build();
        }).toList();
        return new PageResult<>(page, pageSize, histories.getTotalElements(), historyVOList);
    }

    public List<History> getHistoryByUserId(Long userId, Pageable pageable) {
        return historyRepository.findByUserId(userId, pageable).toList();
    }

    public void delete(Long id) {
        historyRepository.deleteById(id);
    }

    public List<History> getAllHistory() {
        return historyRepository.findAll();
    }

    /**
     * 保存浏览历史记录
     *
     * @param newsId 新闻ID
     * @param userId 用户ID
     * @param time   时间
     */
    public void save(Long newsId, Long userId, LocalDateTime time) {
        if (time == null) {
            time = LocalDateTime.now();
        }
        History history = historyRepository.findByUserIdAndNewsId(userId, newsId);
        News news = newsRepository.findById(newsId).orElse(null);
        assert news != null;
        // 保存浏览历史记录
        if (history != null) {
            // 如果已存在浏览记录，则更新时间
            history.setUpdateTime(time);
            historyRepository.save(history);
        } else {
            NewsTag newsTag = newsTagRepository.findById(news.getTagId()).orElse(null);
            assert newsTag != null;
            // 如果不存在浏览记录，则新增
            history = History.builder()
                    .newsId(newsId)
                    .userId(userId)
                    .title(news.getTitle())
                    .mediaId(news.getCreateUser())
                    .mediaName(userService.queryUserById(news.getCreateUser()).getNickname())
                    .cover(news.getCover())
                    .tagId(news.getTagId())
                    .tagName(newsTag.getName())
                    .updateTime(time)
                    .build();
            historyRepository.save(history);
            log.info("用户{}浏览了新闻{}", userId, newsId);
        }

        // 发送消息到kafka
        String data = userId + ":" + newsId;
        kafkaTemplate.send(UserConstant.USER_INTEREST, data, data);
    }

    @KafkaListener(topics = UserConstant.USER_INTEREST)
    private void updateInterest(String message) {
        String[] data = message.split(":");
        Long userId = Long.parseLong(data[0]);
        Long newsId = Long.parseLong(data[1]);
        // 更新用户兴趣词表
        UserInterest interest = userInterestRepository.findByUserId(userId);
        String userInterestJson = interest.getInterest();
        String newsKeywordsJson = newsKeyWordRepository.findByNewsId(newsId).getKeyWord();

        double threshold = 0.7; // 相似度阈值
        double convertValue = 0.7; // 权重转换值

        String newInterestJson = KeyWordUtil.updateKeyWord(userInterestJson, newsKeywordsJson, KeyWordUtil.UPDATE_TYPE.INCREASE, threshold, convertValue);
        interest.setInterest(newInterestJson);
        userInterestRepository.save(interest);
    }
}
