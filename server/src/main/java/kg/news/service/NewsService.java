package kg.news.service;

import kg.news.dto.NewsDTO;
import kg.news.dto.NewsPageQueryDTO;
import kg.news.entity.News;
import kg.news.result.PageResult;
import kg.news.vo.NewsDetailVO;
import kg.news.vo.NewsSummaryVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {
    /**
     * 发布帖子
     * @param newsDTO 新闻信息
     */
    void post(NewsDTO newsDTO);

    /**
     * 删除新闻
     * @param newsId 新闻ID
     */
    void delete(Long newsId);

    /**
     * 查询新闻
     * @param newsPageQueryDTO 查询条件
     * @return 新闻列表
     */
    PageResult<NewsSummaryVO> queryNews(NewsPageQueryDTO newsPageQueryDTO);
    /**
     * 查询新闻
     * @param newsId 新闻ID
     * @return 新闻
     */
    News queryNews(Long newsId);

    /**
     * 查询新闻详情
     * @param newsId 新闻ID
     * @return 新闻详情
     */
    NewsDetailVO queryNewsDetail(Long newsId);

    /**
     * 为新闻或取消
     * @param newsId 新闻ID
     */
    void likeNews(Long newsId);

    /**
     * ”踩“新闻或取消
     * @param newsId 新闻ID
     */
    void dislikeNews(Long newsId);

    /**
     * 获取所有新闻
     * @return 新闻列表
     */
    List<News> getAllNews();

    /**
     * 更新新闻
     * @param news 新闻
     */
    void update(News news);

    /**
     * 获取浏览量最高的新闻
     *
     * @param pageable 分页信息
     * @return 新闻列表
     */
    List<NewsSummaryVO> queryViewHotNews(Pageable pageable);

    /**
     * 获取点赞数最高的新闻
     *
     * @param pageable 分页信息
     * @return 新闻列表
     */
    List<NewsSummaryVO> queryLikedNews(Pageable pageable);

    /**
     * 获取评论数最高的新闻
     *
     * @param pageable 分页信息
     * @return 新闻列表
     */
    List<NewsSummaryVO> queryCommentedNews(Pageable pageable);
}
