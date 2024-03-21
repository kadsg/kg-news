package kg.news.service.impl;

import kg.news.vo.TagSelectionVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@Slf4j
class TagSelectionServiceImplTest {
    @Autowired
    private TagSelectionServiceImpl tagSelectionService;

    @Test
    void getTagSelection() {
        tagSelectionService.getTagSelection(2L);
    }

    @Test
    void saveTagSelection() {
        Set<Long> tags = new HashSet<>();
        tags.add(1L);
        tags.add(2L);
        TagSelectionVO tagSelectionVO = TagSelectionVO.builder().userId(2L).tags(tags).build();
        tagSelectionService.saveTagSelection(tagSelectionVO);
    }
}