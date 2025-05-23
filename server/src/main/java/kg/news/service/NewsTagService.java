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
    PageResult<NewsTagVO> getAllNewsTag();

    /**
     * 删除新闻标签
     * @param id 新闻标签ID
     */
    void deleteNewsTag(Long id);

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

    /**
     * 获取已发布的新闻标签
     * @param userId 用户ID
     * @return 新闻标签列表
     */
    List<NewsTagVO> getPublishedNewsTags(Long userId);

    /**
     * 获取用户收藏的新闻标签
     * @param userId 用户ID
     * @return 新闻标签列表
     */
    List<NewsTagVO> getFavoriteNewsTag(Long userId);
}
