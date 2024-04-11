package kg.news.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 评论查询DTO
 */
@Data
public class CommentQueryDTO implements Serializable {
    /**
     * 评论ID
     */
    private Long commentId;
    /**
     * 新闻ID
     */
    private Long newsId;
    /**
     * 父评论ID
     */
    private Long parentId;
    /**
     * 回复人ID
     */
    private Long replyId;
    /**
     * 评论内容
     */
    private int page;
    /**
     * 评论内容
     */
    private int size;
}
