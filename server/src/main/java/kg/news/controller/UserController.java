package kg.news.controller;

import kg.news.dto.UserQueryDTO;
import kg.news.entity.User;
import kg.news.result.PageResult;
import kg.news.result.Result;
import kg.news.service.UserService;
import kg.news.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;


/**
 * 用户控制器
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 分页查询用户
     * @param userQueryDTO 用户查询条件
     * @return 用户分页列表
     */
    @PostMapping("/list")
    public Result<PageResult<UserVO>> queryUser(@RequestBody UserQueryDTO userQueryDTO) {
        PageResult<UserVO> userVOList = userService.queryUserByRoleId(userQueryDTO);
        return Result.success(userVOList);
    }

    /**
     * 查询用户详情
     * @param id 用户id
     * @return 用户详情
     */
    @GetMapping("/{id}")
    public Result<UserVO> queryUserById(@PathVariable("id") Long id) {
        User user = userService.queryUserById(id);
        UserVO userVO = UserVO.builder().build();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserId(user.getId());
        return Result.success(userVO);
    }
}
