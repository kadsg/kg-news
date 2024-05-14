package kg.news.controller;

import kg.news.dto.FansQueryDTO;
import kg.news.result.PageResult;
import kg.news.result.Result;
import kg.news.service.FollowService;
import kg.news.vo.FansVO;
import org.springframework.web.bind.annotation.*;

/**
 * 粉丝管理
 */
@CrossOrigin
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
    @PostMapping("/list")
    public Result<PageResult<FansVO>> queryFansList(@RequestBody FansQueryDTO fansQueryDTO) {
        PageResult<FansVO> pageResult = followService.queryFansList(fansQueryDTO);
        return Result.success(pageResult);
    }
}
