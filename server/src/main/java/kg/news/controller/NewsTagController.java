package kg.news.controller;

import kg.news.dto.NewsTagDTO;
import kg.news.result.Result;
import kg.news.service.NewsTagService;
import kg.news.vo.NewsTagVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 新闻标签控制器
 */
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
    @GetMapping("newsTags")
    public Result<NewsTagVO> getAllNewsTag() {
        List<Map<Long, String>> newsTags = newsTagService.getAllNewsTag();
        NewsTagVO newsTagVO = NewsTagVO.builder().tags(newsTags).build();
        return Result.success(newsTagVO);
    }

    /**
     * 添加新闻标签
     * @param newsTagDTO 新闻标签信息
     * @return 添加结果
     */
    @PostMapping()
    public Result<Object> addNewsTag(@RequestBody NewsTagDTO newsTagDTO) {
        newsTagService.addNewsTag(newsTagDTO);
        return Result.success();
    }

    /**
     * 删除新闻标签
     * @param newsTagId 新闻标签ID
     * @return 删除结果
     */
    @DeleteMapping()
    public Result<Object> deleteNewsTag(@RequestParam List<Long> newsTagId) {
        newsTagService.deleteNewsTag(newsTagId);
        return Result.success();
    }

    /**
     * 修改新闻标签
     * @param newsTagDTO 新闻标签信息
     * @return 修改结果
     */
    @PutMapping()
    public Result<Object> updateNewsTag(@RequestBody NewsTagDTO newsTagDTO) {
        newsTagService.updateNewsTag(newsTagDTO);
        return Result.success();
    }
}
