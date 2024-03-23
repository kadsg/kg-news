package kg.news.controller.media;

import kg.news.dto.LoginDTO;
import kg.news.entity.User;
import kg.news.result.Result;
import kg.news.service.LoginService;
import kg.news.vo.LoginVO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 媒体账号控制器
 */
@RestController("mediaAccountController")
@RequestMapping("/media")
public class AccountController {
    private final LoginService loginService;
    public AccountController(@Qualifier("mediaLoginServiceImpl") LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 登录
     * @param loginDTO 登录信息
     * @return 登录结果
     */
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

    /**
     * 登出
     * @return 登出结果
     */
    @PostMapping("/logout")
    public Result<Object> logout() {
        return Result.success();
    }

    /**
     * 注册
     * @param loginDTO 注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<LoginVO> register(@RequestBody LoginDTO loginDTO) {
        User user = loginService.register(loginDTO);
        LoginVO loginVO = LoginVO.builder()
                .id(user.getId())
                .build();
        return Result.success(loginVO);
    }
}
