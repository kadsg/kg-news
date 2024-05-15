package kg.news.controller;

import kg.news.dto.FollowQueryDTO;
import kg.news.result.PageResult;
import kg.news.result.Result;
import kg.news.service.FollowService;
import kg.news.vo.FollowVO;
import org.springframework.web.bind.annotation.*;

/**
 * 账号关注
 */
@CrossOrigin
@RestController
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    /**
     * 关注或取消关注
     * @param followId 被关注的账号ID
     * @return 操作结果
     */
    @PutMapping("/{followId}")
    public Result<Object> follow(@PathVariable("followId") Long followId) {
        followService.follow(followId);
        return Result.success();
    }

    /**
     * 查询关注列表
     * @param followQueryDTO 查询条件
     * @return 关注列表
     */
    @PostMapping("/list")
    public Result<PageResult<FollowVO>> queryFollowList(@RequestBody FollowQueryDTO followQueryDTO) {
        PageResult<FollowVO> pageResult = followService.queryFollowList(followQueryDTO);
        return Result.success(pageResult);
    }
}
