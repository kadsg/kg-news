package kg.news.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import kg.news.constant.NewsConstant;
import kg.news.context.BaseContext;
import kg.news.dto.NewsDTO;
import kg.news.dto.NewsPageQueryDTO;
import kg.news.entity.Favorite;
import kg.news.entity.News;
import kg.news.entity.NewsKeyWord;
import kg.news.enumration.OperationType;
import kg.news.exception.NewsException;
import kg.news.mapper.NewsMapper;
import kg.news.repository.FavoriteRepository;
import kg.news.repository.NewsKeyWordRepository;
import kg.news.repository.NewsRepository;
import kg.news.result.PageResult;
import kg.news.service.NewsService;
import kg.news.service.UserService;
import kg.news.utils.KeyWordUtil;
import kg.news.utils.ServiceUtil;
import kg.news.vo.NewsDetailVO;
import kg.news.vo.NewsSummaryVO;
import org.ansj.app.keyword.Keyword;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final UserService userService;
    private final FavoriteRepository favoriteRepository;
    private final NewsKeyWordRepository newsKeyWordRepository;

    public NewsServiceImpl(NewsRepository newsRepository, NewsMapper newsMapper, UserService userService, FavoriteRepository favoriteRepository, NewsKeyWordRepository newsKeyWordRepository) {
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
        this.userService = userService;
        this.favoriteRepository = favoriteRepository;
        this.newsKeyWordRepository = newsKeyWordRepository;
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
        // TODO: 以下步骤非常耗时，需要异步处理！！！
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
        newsList.forEach(newsSummaryVO -> newsSummaryVO.setMediaName(userService.queryUserById(newsSummaryVO.getMediaId()).getNickname()));
        return new PageResult<>(page, pageSize, newsList.getTotal(), newsList.getResult());
    }

    public News queryNews(Long newsId) {
        return newsRepository.findById(newsId).orElse(null);
    }

    public NewsDetailVO queryNewsDetail(Long newsId) {
        News news = newsRepository.findById(newsId).orElse(null);
        if (news == null || news.getDeleteFlag()) {
            throw new NewsException(NewsConstant.NEWS_NOT_FOUND);
        }
        String mediaName = userService.queryUserById(news.getCreateUser()).getNickname();
        return NewsDetailVO.builder()
                .id(news.getId())
                .tagId(news.getTagId())
                .title(news.getTitle())
                .content(news.getContent())
                .mediaId(news.getCreateUser())
                .mediaName(mediaName)
                .createTime(news.getCreateTime())
                .build();
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
}
