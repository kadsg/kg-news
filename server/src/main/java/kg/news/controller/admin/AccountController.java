package kg.news.controller.admin;

import kg.news.dto.LoginDTO;
import kg.news.result.Result;
import kg.news.service.LoginService;
import kg.news.vo.LoginVO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员账号控制器
 */
@CrossOrigin
@RestController("adminAccountController")
@RequestMapping("/admin")
public class AccountController {
    private final LoginService loginService;

    public AccountController(@Qualifier("adminLoginServiceImpl") LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 登录
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = loginService.login(loginDTO);
        return Result.success(loginVO);
    }

    /**
     * 退出
     * @return 退出结果
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
        loginService.register(loginDTO);
        LoginVO loginVO = loginService.login(loginDTO);
        return Result.success(loginVO);
    }
}
