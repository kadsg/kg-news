package kg.news.controller;

import kg.news.dto.NewsDTO;
import kg.news.dto.NewsPageQueryDTO;
import kg.news.result.PageResult;
import kg.news.result.Result;
import kg.news.service.NewsService;
import kg.news.vo.NewsDetailVO;
import kg.news.vo.NewsSummaryVO;
import org.springframework.web.bind.annotation.*;

/**
 * 新闻帖子管理控制器
 */
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
    @PostMapping("")
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
    @GetMapping("")
    public Result<PageResult<NewsSummaryVO>> queryNews(NewsPageQueryDTO newsPageQueryDTO) {
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
}
