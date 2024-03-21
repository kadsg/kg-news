package kg.news.service;

import kg.news.dto.NewsTagDTO;

public interface NewsTagService {
    /**
     * 增加新闻标签
     * @param newsTagDTO 新闻标签
     */
    void addNewsTag(NewsTagDTO newsTagDTO);
}
