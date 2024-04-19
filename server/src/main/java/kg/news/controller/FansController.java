package kg.news.controller;

import kg.news.dto.FansQueryDTO;
import kg.news.result.PageResult;
import kg.news.result.Result;
import kg.news.service.FollowService;
import kg.news.vo.FansVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 粉丝管理
 */
@RestController
@RequestMapping("/fans")
public class FansController {
    private final FollowService followService;

    public FansController(FollowService followService) {
        this.followService = followService;
    }

    /**
     * 查询粉丝列表
     * @param fansQueryDTO 查询条件
     * @return 粉丝列表
     */
    @GetMapping("/list")
    public Result<PageResult<FansVO>> queryFansList(FansQueryDTO fansQueryDTO) {
        PageResult<FansVO> pageResult = followService.queryFansList(fansQueryDTO);
        return Result.success(pageResult);
    }
}
