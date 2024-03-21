package kg.news.service;

import kg.news.vo.TagSelectionVO;

import java.util.Set;

public interface TagSelectionService {
    /**
     * 获取标签选择
     * @param userId 用户id
     * @return 标签选择
     */
    Set<Long> getTagSelection(Long userId);

    /**
     * 保存标签选择
     * @param tagSelectionVO 标签选择
     */
    void saveTagSelection(TagSelectionVO tagSelectionVO);
}
