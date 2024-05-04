package kg.news.service;

import kg.news.dto.NewsTagDTO;
import kg.news.dto.NewsTagQueryDTO;
import kg.news.entity.NewsTag;
import kg.news.result.PageResult;
import kg.news.vo.NewsTagVO;

import java.util.List;

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
    List<NewsTagVO> getAllNewsTag();

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

    /**
     * 查询新闻标签
     * @param tagId 标签ID
     * @return 新闻标签
     */
    NewsTag queryNewsTag(Long tagId);

    /**
     * 分页查询新闻标签
     * @param newsTagQueryDTO 新闻标签查询条件
     * @return 新闻标签列表
     */
    PageResult<NewsTagVO> queryNewsTag(NewsTagQueryDTO newsTagQueryDTO);

    /**
     * 获取新闻标签
     * @param tagId 标签ID
     * @return 新闻标签
     */
    NewsTagVO getNewsTag(Long tagId);
}
