package kg.news.service.impl;

import kg.news.entity.TagSelection;
import kg.news.repository.TagSelectionRepository;
import kg.news.service.TagSelectionService;
import kg.news.vo.TagSelectionVO;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TagSelectionServiceImpl implements TagSelectionService {
    private final TagSelectionRepository tagSelectionRepository;

    public TagSelectionServiceImpl(TagSelectionRepository tagSelectionRepository) {
        this.tagSelectionRepository = tagSelectionRepository;
    }


    public Set<Long> getTagSelection(Long userId) {
        TagSelection tagSelection = tagSelectionRepository.findByUserId(userId);
        String tagIds = tagSelection.getTagIds();
        if (tagIds == null || tagIds.isEmpty()) {
            return null;
        }
        // tagIds以逗号分隔，转换为数组
        String[] tagIdArray = tagIds.split(",");
        Set<Long> tagIdSet = new HashSet<>();
        for (String tagId : tagIdArray) {
            tagIdSet.add(Long.valueOf(tagId));
        }
        return tagIdSet;
    }

    public void saveTagSelection(TagSelectionVO tagSelectionVO) {
        TagSelection tagSelection = tagSelectionRepository.findByUserId(tagSelectionVO.getUserId());
        if (tagSelection == null) {
            tagSelection = TagSelection.builder().build();
            tagSelection.setUserId(tagSelectionVO.getUserId());
        }
        Set<Long> tags = tagSelectionVO.getTags();
        StringBuilder tagIds = new StringBuilder();
        for (Long tagId : tags) {
            tagIds.append(tagId).append(",");
        }
        // 删除最后一个逗号
        tagIds.deleteCharAt(tagIds.length() - 1);
        tagSelection.setTagIds(tagIds.toString());
        tagSelectionRepository.save(tagSelection);
    }
}
