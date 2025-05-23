package kg.news.controller;

import kg.news.dto.NewsTagDTO;
import kg.news.dto.NewsTagQueryDTO;
import kg.news.result.PageResult;
import kg.news.result.Result;
import kg.news.service.NewsTagService;
import kg.news.vo.NewsTagVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 新闻标签控制器
 */
@CrossOrigin
@RestController
@RequestMapping("/newsTag")
public class NewsTagController {
    private final NewsTagService newsTagService;

    public NewsTagController(NewsTagService newsTagService) {
        this.newsTagService = newsTagService;
    }

    /**
     * 获取所有新闻标签
     * @return 新闻标签列表
     */
    @GetMapping("/list")
    public Result<PageResult<NewsTagVO>> getAllNewsTag() {
        PageResult<NewsTagVO> newsTagVOList = newsTagService.getAllNewsTag();
        return Result.success(newsTagVOList);
    }

    /**
     * 分页查询新闻标签
     * @param newsTagQueryDTO 查询条件
     * @return 新闻标签列表
     */
    @PostMapping("/query")
    public Result<PageResult<NewsTagVO>> queryNewsTag(@RequestBody NewsTagQueryDTO newsTagQueryDTO) {
        PageResult<NewsTagVO> newsTagVOList = newsTagService.queryNewsTag(newsTagQueryDTO);
        return Result.success(newsTagVOList);
    }

    /**
     * 获取新闻标签
     * @param tagId 标签ID
     * @return 新闻标签
     */
    @GetMapping("/{tagId}")
    public Result<NewsTagVO> getNewsTag(@PathVariable Long tagId) {
        NewsTagVO newsTagVO = newsTagService.getNewsTag(tagId);
        return Result.success(newsTagVO);
    }

    /**
     * 添加新闻标签
     * @param newsTagDTO 新闻标签信息
     * @return 添加结果
     */
    @PostMapping
    public Result<Object> addNewsTag(@RequestBody NewsTagDTO newsTagDTO) {
        newsTagService.addNewsTag(newsTagDTO);
        return Result.success();
    }

    /**
     * 删除新闻标签
     * @param id 新闻标签ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Object> deleteNewsTag(@PathVariable Long id) {
        newsTagService.deleteNewsTag(id);
        return Result.success();
    }

    /**
     * 修改新闻标签
     * @param newsTagDTO 新闻标签信息
     * @return 修改结果
     */
    @PutMapping
    public Result<Object> updateNewsTag(@RequestBody NewsTagDTO newsTagDTO) {
        newsTagService.updateNewsTag(newsTagDTO);
        return Result.success();
    }

    /**
     * 获取某个用户所发新闻的所有标签
     */
    @GetMapping("/published/{userId}")
    public Result<List<NewsTagVO>> getPublishedNewsTags(@PathVariable Long userId) {
        List<NewsTagVO> newsTagVOList = newsTagService.getPublishedNewsTags(userId);
        return Result.success(newsTagVOList);
    }


    /**
     * 获取某个用户收藏新闻的所有标签
     */
    @GetMapping("/favorite/{userId}")
    public Result<List<NewsTagVO>> getFavoriteNewsTags(@PathVariable Long userId) {
        List<NewsTagVO> newsTagVOList = newsTagService.getFavoriteNewsTag(userId);
        return Result.success(newsTagVOList);
    }
}
