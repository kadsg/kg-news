package kg.news.controller.admin;

import kg.news.dto.LoginDTO;
import kg.news.dto.NewsTagDTO;
import kg.news.dto.RoleDTO;
import kg.news.entity.User;
import kg.news.result.Result;
import kg.news.service.NewsTagService;
import kg.news.service.RoleService;
import kg.news.service.LoginService;
import kg.news.vo.LoginVO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final LoginService loginService;
    private final RoleService roleService;
    private final NewsTagService newsTagService;

    public AdminController(@Qualifier("adminLoginServiceImpl") LoginService loginService, RoleService roleService, NewsTagService newsTagService) {
        this.loginService = loginService;
        this.roleService = roleService;
        this.newsTagService = newsTagService;
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

    @PostMapping("/addRole")
    public Result<Object> addRole(@RequestBody RoleDTO roleDTO) {
        roleService.addRole(roleDTO);
        return Result.success();
    }

    @PostMapping("/addNewsTag")
    public Result<Object> addNewsTag(@RequestBody NewsTagDTO newsTagDTO) {
        newsTagService.addNewsTag(newsTagDTO);
        return Result.success();
    }
}
