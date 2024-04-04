package kg.news.service.impl;

import kg.news.context.BaseContext;
import kg.news.dto.HistoryQueryDTO;
import kg.news.dto.HistorySaveDTO;
import kg.news.entity.History;
import kg.news.entity.News;
import kg.news.entity.NewsTag;
import kg.news.entity.User;
import kg.news.enumration.OperationType;
import kg.news.repository.HistoryRepository;
import kg.news.result.PageResult;
import kg.news.service.HistoryService;
import kg.news.service.NewsService;
import kg.news.service.NewsTagService;
import kg.news.service.UserService;
import kg.news.utils.ServiceUtil;
import kg.news.vo.HistoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final NewsService newsService;
    private final UserService userService;
    private final NewsTagService newsTagService;

    public HistoryServiceImpl(HistoryRepository historyRepository, NewsService newsService, UserService userService, NewsTagService newsTagService) {
        this.historyRepository = historyRepository;
        this.newsService = newsService;
        this.userService = userService;
        this.newsTagService = newsTagService;
    }

    public PageResult<HistoryVO> queryHistory(HistoryQueryDTO historyQueryDTO) {
        int page = historyQueryDTO.getPage();
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
        return new PageResult<>(histories.getTotalElements(), historyVOList);
    }

    public void delete(Long id) {
        historyRepository.findById(id).ifPresent(history -> {
            try {
                ServiceUtil.autoFill(history, OperationType.UPDATE);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            historyRepository.save(history);
        }
        );
    }

    public void save(HistorySaveDTO historySaveDTO) {
        Long newsId = historySaveDTO.getNewsId();
        Long userId = BaseContext.getCurrentId();
        History history = historyRepository.findByUserIdAndNewsId(userId, newsId);
        if (history != null) {
            // 如果已存在浏览记录，则更新时间
            history.setUpdateTime(LocalDateTime.now());
            historyRepository.save(history);
            return;
        }
        history = new History();
        BeanUtils.copyProperties(historySaveDTO, history);
        history.setUserId(userId);
        historyRepository.save(history);
    }
}
