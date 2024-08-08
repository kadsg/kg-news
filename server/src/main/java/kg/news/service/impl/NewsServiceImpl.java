package kg.news.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import kg.news.constant.NewsConstant;
import kg.news.context.BaseContext;
import kg.news.dto.FavoriteQueryDTO;
import kg.news.dto.HistorySaveDTO;
import kg.news.dto.NewsDTO;
import kg.news.dto.NewsPageQueryDTO;
import kg.news.entity.*;
import kg.news.enumration.OperationType;
import kg.news.exception.NewsException;
import kg.news.mapper.FavoriteMapper;
import kg.news.mapper.NewsMapper;
import kg.news.repository.*;
import kg.news.result.PageResult;
import kg.news.service.NewsService;
import kg.news.service.RecommendService;
import kg.news.service.UserService;
import kg.news.utils.KeyWordUtil;
import kg.news.utils.RedisUtils;
import kg.news.utils.ServiceUtil;
import kg.news.vo.NewsDetailVO;
import kg.news.vo.NewsLikeStatusVO;
import kg.news.vo.NewsSummaryVO;
import lombok.extern.slf4j.Slf4j;
import org.ansj.app.keyword.Keyword;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    @Resource
    KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private NewsRepository newsRepository;
    @Resource
    private NewsMapper newsMapper;
    @Resource
    private UserService userService;
    @Resource
    private FavoriteRepository favoriteRepository;
    @Resource
    private NewsKeyWordRepository newsKeyWordRepository;
    @Resource
    private HistoryRepository historyRepository;
    @Resource
    private RoleMapperRepository roleMapperRepository;
    @Resource
    private NewsTagRepository newsTagRepository;
    @Resource
    private UserInterestRepository userInterestRepository;
    @Resource
    private FavoriteMapper favoriteMapper;
    @Resource
    private RecommendService recommendService;
    // 定时任务线程池
    private final ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);
    // 并发安全的Map，用于暂存点赞任务
    private final ConcurrentHashMap<String, ScheduledFuture<?>> likeTasks = new ConcurrentHashMap<>();
    // 并发安全的Map，用于暂存点赞的点击次数
    private final ConcurrentHashMap<String, Short> likeClickMap = new ConcurrentHashMap<>();

    @Transactional
    @CacheEvict(cacheNames = NewsConstant.NEWS + ':', allEntries = true)
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
        generateKeyWords(save);
    }


    /**
     * 生成新闻关键词（发送）
     *
     * @param news 新闻
     */
    @Async
    protected void generateKeyWords(News news) {
        kafkaTemplate.send(NewsConstant.NEWS_TOPIC, String.valueOf(news.getId()), JSONObject.toJSONString(news));
        log.info("成功发送消息，新闻标题为：{}", news.getTitle());
    }

    /**
     * 监听数据并处理
     *
     * @param newsMessage 监听到的新闻
     */
    @KafkaListener(topics = NewsConstant.NEWS_TOPIC)
    protected void process(String newsMessage) {
        News news = JSONObject.parseObject(newsMessage, News.class);
        log.info("监听到消息，新闻标题为：{}", news.getTitle());
        // 提取关键词
        List<Keyword> keyWordList = KeyWordUtil.getKeyWordList(news.getTitle(), news.getContent(), 5);
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
                    .newsId(news.getId())
                    .keyWord(asString)
                    .build();
            // 保存该关键词
            newsKeyWordRepository.save(newsKeyWord);
            log.info("新闻关键字保存成功，新闻：{}，关键词：{}", news.getTitle(), newsKeyWord.getKeyWord());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    @Cacheable(cacheNames = NewsConstant.NEWS + ':',
            key = "#newsPageQueryDTO.newsTagId + '_' + #newsPageQueryDTO.pageNum + '_' + #newsPageQueryDTO.pageSize",
            unless = "#result.list.size() == 0 OR #newsPageQueryDTO.newsTagId == 0")
    public PageResult<NewsSummaryVO> queryNews(NewsPageQueryDTO newsPageQueryDTO) {
        if (newsPageQueryDTO.getNewsTagId() != null && newsPageQueryDTO.getNewsTagId() == 0) {
            return getRecommendNews(BaseContext.getCurrentId());
        }

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
    @CacheEvict(cacheNames = NewsConstant.NEWS + ':', allEntries = true)
    public NewsDetailVO queryNewsDetail(Long newsId) {
        // 1. 查询新闻详情
        News news = newsRepository.findById(newsId).orElse(null);
        if (news == null || news.getDeleteFlag()) {
            throw new NewsException(NewsConstant.NEWS_NOT_FOUND);
        }
        recommendService.removeRecommend(BaseContext.getCurrentId(), newsId);
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


    public void likeNews(Long newsId) {
        News news = newsRepository.findById(newsId).orElse(null);
        Long userId = BaseContext.getCurrentId();

        if (news == null || news.getDeleteFlag()) {
            throw new NewsException(NewsConstant.NEWS_NOT_FOUND);
        }

        String taskKey = userId + ":" + newsId;
        ScheduledFuture<?> scheduledFuture = likeTasks.get(taskKey);
        // 如果任务已存在且未完成，则取消之前的任务
        if (scheduledFuture != null && !scheduledFuture.isDone()) {
            scheduledFuture.cancel(true);
        }
        // 将当前任务添加到线程池
        // 5秒后执行任务
        ScheduledFuture<?> schedule = scheduledThreadPool.schedule(() -> executeLikeNews(newsId, userId), 5, TimeUnit.SECONDS);
        // 将任务放入Map
        likeTasks.put(taskKey, schedule);
        // 记录点赞的点击次数
        Short i = likeClickMap.get(taskKey);
        if (i == null || i == 0) {
            likeClickMap.put(taskKey, (short) 1);
        } else {
            likeClickMap.put(taskKey, (short) (i + 1));
        }
//        log.info("用户 {} 对新闻 {} 的点赞任务已加入队列", userId, newsId);
    }

    private void executeLikeNews(Long newsId, Long userId) {
        News news = newsRepository.findById(newsId).orElse(null);
        if (news == null || news.getDeleteFlag()) {
            throw new NewsException(NewsConstant.NEWS_NOT_FOUND);
        }

        // 如果点击次数为偶数，则不执行
        String taskKey = userId + ":" + newsId;
        if (likeClickMap.get(taskKey) % 2 == 0) {
            log.info("无效点击，已终止用户 {} 对新闻 {} 的点赞任务", userId, newsId);
            // 情况计时器
            likeClickMap.remove(taskKey);
            log.info("清空 {} 的计数器", taskKey);
            return;
        }

        // 开始执行，情况计时器
        likeClickMap.remove(taskKey);
        String likeHashKey = NewsConstant.NEWS_LIKE + ":" + newsId;
        String lockKey = NewsConstant.NEWS_LIKE_USER_LOCK + ':' + newsId + ':' + userId;
        short likeCount;

        // 不必加锁
        // 即便读取到旧数据也不影响，因为会进行校验
        Object likeCache = redisUtils.hmGet(likeHashKey, userId);
        if (likeCache == null) {
            Favorite favorite = favoriteRepository.findByNewsIdAndUserId(newsId, userId);
            if (favorite != null) {
                boolean likeFlag = favorite.isFavorFlag();
                likeCount = (short) (likeFlag ? -1 : 1);
            } else {
                likeCount = 1;
            }
        } else {
            String[] split = likeCache.toString().split(":");
            likeCount = Short.parseShort(split[0]);
            likeCount = (short) ((likeCount > 0) ? -1 : 1);
        }

        if (likeCount > 0) {
            log.info("用户 {} 点赞了新闻 {} ", userId, newsId);
        } else {
            log.info("用户 {} 取消点赞了新闻 {} ", userId, newsId);
        }
        // 加锁，防止删除数据前的一瞬间写入新的数据
        // 此种做法性能较好，缺点是会产生短时间内的数据不一致，且内存中锁的数量会增加
        while (!redisUtils.lock(lockKey, Thread.currentThread(), 5L)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        redisUtils.hmSet(likeHashKey, userId, likeCount);
        // 任务执行完毕
    }

    @Transactional
    @CacheEvict(cacheNames = NewsConstant.NEWS + ':', allEntries = true)
    public void dislikeNews(Long newsId) {
        News news = newsRepository.findById(newsId).orElse(null);
        Long userId = BaseContext.getCurrentId();
        Favorite favorite = favoriteRepository.findByNewsIdAndUserId(newsId, userId);

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

    public PageResult<NewsSummaryVO> getFavoriteNews(FavoriteQueryDTO favoriteQueryDTO) {
        int pageNum = favoriteQueryDTO.getPageNum();
        int pageSize = favoriteQueryDTO.getPageSize();
        if (pageNum <= 0 || pageSize <= 0) {
            pageNum = 1;
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        Page<NewsSummaryVO> newsPage = favoriteMapper.queryFavoriteNews(favoriteQueryDTO);
        return new PageResult<>(newsPage.getPageNum(), newsPage.getPageSize(), newsPage.getTotal(), newsPage.getResult());
    }

    public PageResult<NewsSummaryVO> getRecommendNews(Long userId) {
        List<NewsSummaryVO> newsList = recommendService.getRecommendationByUserId(userId);
        return new PageResult<>(1, 1000, newsList.size(), newsList);
    }

    /**
     * 保存浏览历史记录
     *
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

    /**
     * 删除已执行的任务
     */
    @Scheduled(fixedRate = 5000)
    public void removeDoneTask() {
        likeTasks.forEach((key, value) -> {
            if (value.isDone()) {
                log.info("用户 {} 对新闻 {} 的点赞任务已完成，正在移除...", key.split(":")[0], key.split(":")[1]);
                likeTasks.remove(key);
            }
        });
    }
}
