package kg.news.controller.user;

import kg.news.result.Result;
import kg.news.service.NewsTagService;
import kg.news.vo.NewsTagVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 新闻标签控制器（用户）
 */
@RestController("userNewsTagController")
@RequestMapping("/user")
public class NewsTagController {
    private final NewsTagService newsTagService;

    public NewsTagController(NewsTagService newsTagService) {
        this.newsTagService = newsTagService;
    }

    /**
     * 获取所有新闻标签
     * @return 新闻标签列表
     */
    @GetMapping("/newsTags")
    public Result<NewsTagVO> getAllNewsTag() {
        List<Map<Long, String>> newsTags = newsTagService.getAllNewsTag();
        NewsTagVO newsTagVO = NewsTagVO.builder().tags(newsTags).build();
        return Result.success(newsTagVO);
    }
}
