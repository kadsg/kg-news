package kg.news.controller;

import kg.news.dto.FavoriteQueryDTO;
import kg.news.dto.NewsDTO;
import kg.news.dto.NewsPageQueryDTO;
import kg.news.result.PageResult;
import kg.news.result.Result;
import kg.news.service.NewsService;
import kg.news.vo.NewsDetailVO;
import kg.news.vo.NewsLikeStatusVO;
import kg.news.vo.NewsSummaryVO;
import org.springframework.web.bind.annotation.*;

/**
 * 新闻控制器
 */
@CrossOrigin
@RestController
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * 发布帖子
     * @param newsDTO 帖子信息
     * @return 发布结果
     */
    @PostMapping("/post")
    public Result<Object> post(@RequestBody NewsDTO newsDTO) {
        newsService.post(newsDTO);
        return Result.success();
    }

    /**
     * 删除新闻
     * @param newsId 新闻ID
     * @return 删除结果
     */
    @DeleteMapping("/{newsId}")
    public Result<Object> delete(@PathVariable Long newsId) {
        newsService.delete(newsId);
        return Result.success();
    }

    /**
     * 查询新闻简介
     * @param newsPageQueryDTO 查询条件
     * @return 新闻列表
     */
    @PostMapping("/list")
    public Result<PageResult<NewsSummaryVO>> queryNews(@RequestBody NewsPageQueryDTO newsPageQueryDTO) {
        PageResult<NewsSummaryVO> newsSummaryVOList = newsService.queryNews(newsPageQueryDTO);
        return Result.success(newsSummaryVOList);
    }

    /**
     * 查询新闻详情
     * @param newsId 新闻ID
     * @return 新闻详情
     */
    @GetMapping("/detail")
    public Result<NewsDetailVO> queryNewsDetail(@RequestParam Long newsId) {
        NewsDetailVO newsDetailVO = newsService.queryNewsDetail(newsId);
        return Result.success(newsDetailVO);
    }

    /**
     * 为新闻点赞或取消
     * @param newsId 新闻ID
     * @return 点赞结果
     */
    @PutMapping("/like/{id}")
    public Result<Object> likeNews(@PathVariable("id") Long newsId) {
        newsService.likeNews(newsId);
        return Result.success();
    }

    /**
     * 为新闻点踩或取消
     * @param newsId 新闻ID
     * @return 点踩结果
     */
    @PutMapping("/dislike/{id}")
    public Result<Object> dislikeNews(@PathVariable("id") Long newsId) {
        newsService.dislikeNews(newsId);
        return Result.success();
    }

    /**
     * 获取新闻收藏状态
     * @param newsId 新闻ID
     * @return 点赞状态
     */
    @GetMapping("/like/status/{id}")
    public Result<NewsLikeStatusVO> getNewsLikeStatus(@PathVariable("id") Long newsId) {
        NewsLikeStatusVO newsLikeStatusVO = newsService.getNewsLikeStatus(newsId);
        return Result.success(newsLikeStatusVO);
    }

    /**
     * 获取收藏新闻
     * @param favoriteQueryDTO 查询条件
     * @return 新闻列表
     */
    @PostMapping("/favorite")
    public Result<PageResult<NewsSummaryVO>> getFavoriteNews(@RequestBody FavoriteQueryDTO favoriteQueryDTO) {
        PageResult<NewsSummaryVO> newsSummaryVOList = newsService.getFavoriteNews(favoriteQueryDTO);
        return Result.success(newsSummaryVOList);
    }

    /**
     * 获取推荐新闻
     * @param userId 用户ID
     * @return 新闻列表
     */
    @GetMapping("/recommend/{id}")
    public Result<PageResult<NewsSummaryVO>> getRecommendNews(@PathVariable("id") Long userId) {
        PageResult<NewsSummaryVO> newsSummaryVOList = newsService.getRecommendNews(userId);
        return Result.success(newsSummaryVOList);
    }
}
