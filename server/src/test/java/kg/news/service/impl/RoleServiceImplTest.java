package kg.news.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class RoleServiceImplTest {

    @Autowired
    private RoleServiceImpl roleService;

    @Test
    void getRole() {
        log.info("role: {}", roleService.getRole("admin"));
        log.info("role: {}", roleService.getRole("media"));
        log.info("role: {}", roleService.getRole("user"));
    }
}