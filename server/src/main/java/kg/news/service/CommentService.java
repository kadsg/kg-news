package kg.news.service;

import kg.news.dto.CommentQueryDTO;
import kg.news.dto.CommentSaveDTO;
import kg.news.result.PageResult;
import kg.news.vo.CommentVO;

import java.lang.reflect.InvocationTargetException;

public interface CommentService {
    /**
     * 获取新闻评论列表
     *
     * @param commentDTO 评论查询DTO
     * @return 新闻评论列表
     */
    PageResult<CommentVO> queryNewsCommentList(CommentQueryDTO commentDTO);

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     */
    void deleteComment(Long commentId) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;

    /**
     * 点赞评论或取消
     *
     * @param commentId 评论ID
     */
    void likeComment(Long commentId);

    /**
     * 评论
     *
     * @param commentSaveDTO 评论保存DTO
     */
    void comment(CommentSaveDTO commentSaveDTO) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;

    /**
     * ”踩“评论或取消
     *
     * @param commentId 评论ID
     */
    void dislikeComment(Long commentId);
}
