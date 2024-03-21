package kg.news.service.impl;

import kg.news.dto.NewsTagDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class NewsTagServiceImplTest {
    @Autowired
    private NewsTagServiceImpl newsTagService;

    @Test
    void addNewsTag() {
        NewsTagDTO tagDTO1 = NewsTagDTO.builder().name("体育").build();
        NewsTagDTO tagDTO2 = NewsTagDTO.builder().name("时政").build();
        newsTagService.addNewsTag(tagDTO1);
        newsTagService.addNewsTag(tagDTO2);
    }
}