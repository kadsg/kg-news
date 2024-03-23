package kg.news.service;

import kg.news.dto.NewsTagDTO;

import java.util.List;
import java.util.Map;

public interface NewsTagService {
    /**
     * 增加新闻标签
     * @param newsTagDTO 新闻标签
     */
    void addNewsTag(NewsTagDTO newsTagDTO);

    /**
     * 获取所有新闻标签
     * @return 新闻标签列表
     */
    List<Map<Long, String>> getAllNewsTag();

    /**
     * 删除新闻标签
     * @param newsTagId 新闻标签ID
     */
    void deleteNewsTag(List<Long> newsTagId);

    /**
     * 修改新闻标签
     * @param newsTagDTO 新闻标签
     */
    void updateNewsTag(NewsTagDTO newsTagDTO);
}
