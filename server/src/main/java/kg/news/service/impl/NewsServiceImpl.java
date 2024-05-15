package kg.news.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import kg.news.constant.NewsConstant;
import kg.news.context.BaseContext;
import kg.news.dto.HistorySaveDTO;
import kg.news.dto.NewsDTO;
import kg.news.dto.NewsPageQueryDTO;
import kg.news.entity.*;
import kg.news.enumration.OperationType;
import kg.news.exception.NewsException;
import kg.news.mapper.NewsMapper;
import kg.news.repository.*;
import kg.news.result.PageResult;
import kg.news.service.NewsService;
import kg.news.service.UserService;
import kg.news.utils.KeyWordUtil;
import kg.news.utils.ServiceUtil;
import kg.news.vo.NewsDetailVO;
import kg.news.vo.NewsLikeStatusVO;
import kg.news.vo.NewsSummaryVO;
import lombok.extern.slf4j.Slf4j;
import org.ansj.app.keyword.Keyword;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(15); // Change this number based on your needs

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final UserService userService;
    private final FavoriteRepository favoriteRepository;
    private final NewsKeyWordRepository newsKeyWordRepository;
    private final HistoryRepository historyRepository;
    private final RoleMapperRepository roleMapperRepository;
    private final NewsTagRepository newsTagRepository;
    private final UserInterestRepository userInterestRepository;

    public NewsServiceImpl(NewsRepository newsRepository, NewsMapper newsMapper, UserService userService, FavoriteRepository favoriteRepository, NewsKeyWordRepository newsKeyWordRepository, HistoryRepository historyRepository, RoleMapperRepository roleMapperRepository,
                           NewsTagRepository newsTagRepository, UserInterestRepository userInterestRepository) {
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
        this.userService = userService;
        this.favoriteRepository = favoriteRepository;
        this.newsKeyWordRepository = newsKeyWordRepository;
        this.historyRepository = historyRepository;
        this.roleMapperRepository = roleMapperRepository;
        this.newsTagRepository = newsTagRepository;
        this.userInterestRepository = userInterestRepository;
    }

    @Transactional
    public void post(NewsDTO newsDTO) {
        News news = News.builder()
                .tagId(newsDTO.getTagId())
                .title(newsDTO.getTitle())
                .content(newsDTO.getContent())
                .cover(newsDTO.getCover())
                .build();
        try {
            ServiceUtil.autoFill(news, OperationType.INSERT);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        News save = newsRepository.save(news);

        executorService.submit(() -> {
            // 提取关键词
            List<Keyword> keyWordList = KeyWordUtil.getKeyWordList(newsDTO.getTitle(), newsDTO.getContent(), 5);
            // 构造关键词权重Map
            Map<String, Double> keyWordMap = new HashMap<>();
            keyWordList.forEach(keyword -> {
                String key = keyword.getName();
                Double weight = keyword.getScore();
                keyWordMap.put(key, weight);
            });
            System.out.println("关键词权重Map：" + keyWordMap);
            // 关键词权重Map示例：
            // {乌方=138.15585324319431, 乌多=144.2370822012652, 大规模=55.26272908435627, 俄军=55.26262506735223, 设施=55.26206753491392}
            // 将其转换为JSON字符串
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String asString = objectMapper.writeValueAsString(keyWordMap);
                NewsKeyWord newsKeyWord = NewsKeyWord.builder()
                        .newsId(save.getId())
                        .keyWord(asString)
                        .build();
                // 保存该关键词
                newsKeyWordRepository.save(newsKeyWord);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void delete(Long newsId) {
        News news = newsRepository.findById(newsId).orElse(null);
        if (news == null) {
            throw new NewsException(NewsConstant.NEWS_NOT_FOUND);
        }
        if (news.getDeleteFlag()) {
            throw new NewsException(NewsConstant.NEWS_NOT_FOUND);
        }
        news.setDeleteFlag(true);
        try {
            ServiceUtil.autoFill(news, OperationType.UPDATE);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        newsRepository.save(news);
    }

    public PageResult<NewsSummaryVO> queryNews(NewsPageQueryDTO newsPageQueryDTO) {
        int page = newsPageQueryDTO.getPageNum();
        int pageSize = newsPageQueryDTO.getPageSize();
        if (page <= 0 || pageSize <= 0) {
            // 页码和每页大小必须大于0，未指定则默认为1和10
            page = 1;
            pageSize = 10;
        }
        PageHelper.startPage(page, pageSize);
        Page<NewsSummaryVO> newsList = newsMapper.queryNews(newsPageQueryDTO);
        newsList.forEach(newsSummaryVO ->
                newsSummaryVO.setMediaName(userService.queryUserById(newsSummaryVO.getMediaId()).getNickname()));
        return new PageResult<>(page, pageSize, newsList.getTotal(), newsList.getResult());
    }

    public News queryNews(Long newsId) {
        return newsRepository.findById(newsId).orElse(null);
    }

    @Transactional
    public NewsDetailVO queryNewsDetail(Long newsId) {
        // 1. 查询新闻详情
        News news = newsRepository.findById(newsId).orElse(null);
        if (news == null || news.getDeleteFlag()) {
            throw new NewsException(NewsConstant.NEWS_NOT_FOUND);
        }
        String mediaName = userService.queryUserById(news.getCreateUser()).getNickname();
        NewsDetailVO newsDetailVO = NewsDetailVO.builder()
                .id(news.getId())
                .tagId(news.getTagId())
                .title(news.getTitle())
                .content(news.getContent())
                .mediaId(news.getCreateUser())
                .mediaName(mediaName)
                .viewCount(news.getViewCount() + 1)
                .likeCount(news.getLikeCount())
                .unlikeCount(news.getUnlikeCount())
                .commentCount(news.getCommentCount())
                .createTime(news.getCreateTime())
                .build();

        Long currentId = BaseContext.getCurrentId();

        // TODO: 使用多线程将导致死锁,原因未知
        // 2. 保存浏览记录
        RoleMapper roleMapper = roleMapperRepository.findByUserId(currentId);
        // 如果是普通用户，则保存浏览记录
        if (roleMapper.getRoleId() == 3L) {
            NewsTag newsTag = newsTagRepository.findById(news.getTagId()).orElse(null);
            assert newsTag != null;
            HistorySaveDTO historySaveDTO = new HistorySaveDTO();

            BeanUtils.copyProperties(news, historySaveDTO);
            historySaveDTO.setMediaId(news.getCreateUser());
            historySaveDTO.setMediaName(mediaName);
            historySaveDTO.setNewsId(news.getId());
            historySaveDTO.setCover(news.getCover());
            historySaveDTO.setTagName(newsTag.getName());
            historySaveDTO.setUserId(currentId);

            save(historySaveDTO);
        }

        return newsDetailVO;
    }

    @Transactional
    public void likeNews(Long newsId) {
        News news = newsRepository.findById(newsId).orElse(null);
        Long userId = BaseContext.getCurrentId();
        Favorite favorite = favoriteRepository.findById(newsId).orElse(null);

        if (news == null || news.getDeleteFlag()) {
            throw new NewsException(NewsConstant.NEWS_NOT_FOUND);
        }
        // 如果没有记录，则创建一条
        if (favorite == null) {
            favorite = Favorite.builder()
                    .newsId(newsId)
                    .userId(userId)
                    .favorFlag(true)
                    .build();
            news.setLikeCount(news.getLikeCount() + 1);
        } else {
            // 如果该记录为“点赞“，此时进行取消点赞操作
            if (favorite.isFavorFlag()) {
                news.setLikeCount(news.getLikeCount() - 1);
            } else {
                // 如果该记录为”取消点赞“，此时进行点赞操作
                news.setLikeCount(news.getLikeCount() + 1);
            }
            favorite.setFavorFlag(!favorite.isFavorFlag());
            // 如果有点踩记录，则取消
            if (favorite.isDislikeFlag()) {
                favorite.setDislikeFlag(false);
                news.setUnlikeCount(news.getUnlikeCount() - 1);
            }
        }
        favoriteRepository.save(favorite);
        newsRepository.save(news);
    }

    @Transactional
    public void dislikeNews(Long newsId) {
        News news = newsRepository.findById(newsId).orElse(null);
        Long userId = BaseContext.getCurrentId();
        Favorite favorite = favoriteRepository.findById(newsId).orElse(null);

        if (news == null || news.getDeleteFlag()) {
            throw new NewsException(NewsConstant.NEWS_NOT_FOUND);
        }
        // 如果没有记录，则创建一条
        if (favorite == null) {
            favorite = Favorite.builder()
                    .newsId(newsId)
                    .userId(userId)
                    .dislikeFlag(true)
                    .build();
            news.setUnlikeCount(news.getUnlikeCount() + 1);
        } else {
            // 如果该记录为“点踩“，此时进行取消点踩操作
            if (favorite.isDislikeFlag()) {
                news.setUnlikeCount(news.getUnlikeCount() - 1);
            } else {
                // 如果该记录为”取消点踩“，此时进行点踩操作
                news.setUnlikeCount(news.getUnlikeCount() + 1);
            }
            favorite.setDislikeFlag(!favorite.isDislikeFlag());
            // 如果有点赞记录，则取消
            if (favorite.isFavorFlag()) {
                favorite.setFavorFlag(false);
                news.setLikeCount(news.getLikeCount() - 1);
            }
        }
        favoriteRepository.save(favorite);
        newsRepository.save(news);
    }

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public void update(News news) {
        try {
            ServiceUtil.autoFill(news, OperationType.UPDATE);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        newsRepository.save(news);
    }

    public List<NewsSummaryVO> queryViewHotNews(Pageable pageable) {
        // Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        // 映射到MyBatis的分页插件
        int pageNum = pageable.getPageNumber() + 1;
        int pageSize = pageable.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        return newsMapper.findTopByViewCount();
    }

    public List<NewsSummaryVO> queryLikedNews(Pageable pageable) {
        // Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        // 映射到MyBatis的分页插件
        int pageNum = pageable.getPageNumber() + 1;
        int pageSize = pageable.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        return newsMapper.findTopByLikeCount();
    }

    public List<NewsSummaryVO> queryCommentedNews(Pageable pageable) {
        // Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        // 映射到MyBatis的分页插件
        int pageNum = pageable.getPageNumber() + 1;
        int pageSize = pageable.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        return newsMapper.findTopByCommentCount();
    }

    public NewsLikeStatusVO getNewsLikeStatus(Long newsId) {
        Long userId = BaseContext.getCurrentId();
        Favorite favorite = favoriteRepository.findByNewsIdAndUserId(newsId, userId);
        if (favorite == null) {
            return NewsLikeStatusVO.builder()
                    .userId(userId)
                    .newsId(newsId)
                    .likeStatus(false)
                    .dislikeStatus(false)
                    .build();
        }
        return NewsLikeStatusVO.builder()
                .userId(userId)
                .newsId(newsId)
                .likeStatus(favorite.isFavorFlag())
                .dislikeStatus(favorite.isDislikeFlag())
                .build();
    }

    /**
     * 保存浏览历史记录
     * @param historySaveDTO 浏览历史记录
     */
    private void save(HistorySaveDTO historySaveDTO) {
        Long newsId = historySaveDTO.getNewsId();
        Long userId = historySaveDTO.getUserId();
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
            history.setUpdateTime(LocalDateTime.now());
            historyRepository.save(history);
            log.info("用户{}浏览了新闻{}", userId, newsId);
        }

        // 更新新闻浏览次数
        News news = queryNews(newsId);
        news.setViewCount(news.getViewCount() + 1);
        update(news);

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
