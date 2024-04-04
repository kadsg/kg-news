package kg.news.controller;


import kg.news.result.Result;
import kg.news.service.TagSelectionService;
import kg.news.vo.TagSelectionVO;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 标签选择控制器
 */
@RestController
@RequestMapping("/tagSelection")
public class TagSelectionController {
    private final TagSelectionService tagSelectionService;

    public TagSelectionController(TagSelectionService tagSelectionService) {
        this.tagSelectionService = tagSelectionService;
    }

    /**
     * 获取标签选择
     * @param id 用户id
     * @return 标签选择
     */
    @GetMapping("/{id}")
    public Result<TagSelectionVO> getTagSelection(@PathVariable("id") Long id) {
        Set<Long> tagSelection = tagSelectionService.getTagSelection(id);
        TagSelectionVO tagSelectionVO = TagSelectionVO.builder()
                .userId(id)
                .tags(tagSelection)
                .build();
        return Result.success(tagSelectionVO);
    }

    /**
     * 保存标签选择
     * @param tagSelectionVO 标签选择
     * @return 保存结果
     */
    @PutMapping()
    public Result<Object> saveTagSelection(@RequestBody TagSelectionVO tagSelectionVO) {
        tagSelectionService.saveTagSelection(tagSelectionVO);
        return Result.success();
    }
}
