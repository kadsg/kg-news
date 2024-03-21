package kg.news.controller.user;

import kg.news.dto.LoginDTO;
import kg.news.entity.User;
import kg.news.result.Result;
import kg.news.service.LoginService;
import kg.news.service.TagSelectionService;
import kg.news.vo.LoginVO;
import kg.news.vo.TagSelectionVO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    private final LoginService loginService;
    private final TagSelectionService tagSelectionService;

    public UserController(@Qualifier("userLoginServiceImpl") LoginService loginService, TagSelectionService tagSelectionService) {
        this.loginService = loginService;
        this.tagSelectionService = tagSelectionService;
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        User user = loginService.login(loginDTO);
        String token = loginService.getToken(user);
        LoginVO loginVO = LoginVO.builder()
                .id(user.getId())
                .token(token)
                .build();
        return Result.success(loginVO);
    }

    @PostMapping("/logout")
    public Result<Object> logout() {
        return Result.success();
    }

    @PostMapping("/register")
    public Result<LoginVO> register(@RequestBody LoginDTO loginDTO) {
        User user = loginService.register(loginDTO);
        LoginVO loginVO = LoginVO.builder()
                .id(user.getId())
                .build();
        return Result.success(loginVO);
    }

    /**
     * 获取标签选择
     * @param id 用户id
     * @return 标签选择
     */
    @GetMapping("/tagSelection/{id}")
    public Result<TagSelectionVO> getTagSelection(@PathVariable("id") Long id) {
        Set<Long> tagSelection = tagSelectionService.getTagSelection(id);
        TagSelectionVO tagSelectionVO = TagSelectionVO.builder()
                .userId(id)
                .tags(tagSelection)
                .build();
        return Result.success(tagSelectionVO);
    }

    @PutMapping("/tagSelection")
    public Result<Object> saveTagSelection(@RequestBody TagSelectionVO tagSelectionVO) {
        tagSelectionService.saveTagSelection(tagSelectionVO);
        return Result.success();
    }
}
