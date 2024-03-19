package kg.news.service.impl;

import kg.news.dto.LoginDTO;
import kg.news.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Slf4j
class UserLoginServiceImplTest {
    @Autowired
    private UserLoginServiceImpl loginService;

    @Test
    void login() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("kadsg0769@163.com");
        loginDTO.setPassword("123456");
        User user = loginService.login(loginDTO);
        log.info("user login: {}", user);
    }

    @Test
    void getToken() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("kadsg0769@163.com");
        loginDTO.setPassword("123456");
        User user = loginService.login(loginDTO);
        String token = loginService.getToken(user);
        log.info("user token: {}", token);
    }

    @Test
    void register() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("ming@163.com");
        loginDTO.setPassword("123456");
        User user = loginService.register(loginDTO);
        log.info("user register: {}", user);
    }
}