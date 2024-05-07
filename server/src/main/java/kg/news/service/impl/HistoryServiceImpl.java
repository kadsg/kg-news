package kg.news.service.impl;

import kg.news.context.BaseContext;
import kg.news.dto.HistoryQueryDTO;
import kg.news.dto.HistorySaveDTO;
import kg.news.entity.*;
import kg.news.repository.HistoryRepository;
import kg.news.repository.NewsKeyWordRepository;
import kg.news.repository.UserInterestRepository;
import kg.news.result.PageResult;
import kg.news.service.HistoryService;
import kg.news.service.NewsService;
import kg.news.service.NewsTagService;
import kg.news.service.UserService;
import kg.news.utils.KeyWordUtil;
import kg.news.vo.HistoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final NewsService newsService;
    private final UserService userService;
    private final NewsTagService newsTagService;
    private final UserInterestRepository userInterestRepository;
    private final NewsKeyWordRepository newsKeyWordRepository;

    public HistoryServiceImpl(HistoryRepository historyRepository, NewsService newsService, UserService userService, NewsTagService newsTagService, UserInterestRepository userInterestRepository, NewsKeyWordRepository newsKeyWordRepository) {
        this.historyRepository = historyRepository;
        this.newsService = newsService;
        this.userService = userService;
        this.newsTagService = newsTagService;
        this.userInterestRepository = userInterestRepository;
        this.newsKeyWordRepository = newsKeyWordRepository;
    }

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

    @Transactional
    public void save(HistorySaveDTO historySaveDTO) {
        Long newsId = historySaveDTO.getNewsId();
        Long userId = BaseContext.getCurrentId();
        History history = historyRepository.findByUserIdAndNewsId(userId, newsId);
        // 保存浏览历史记录
        if (history != null) {
            // 如果已存在浏览记录，则更新时间
            history.setUpdateTime(LocalDateTime.now());
            historyRepository.save(history);
        } else {
            // 如果不存在浏览记录，则新增
            history = new History();
            BeanUtils.copyProperties(historySaveDTO, history);
            history.setUserId(userId);
            historyRepository.save(history);

            // TODO: 此处开始异步
            // 更新新闻浏览次数
            News news = newsService.queryNews(newsId);
            news.setViewCount(news.getViewCount() + 1);
            newsService.update(news);

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
}
