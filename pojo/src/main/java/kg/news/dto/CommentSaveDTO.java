package kg.news.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentSaveDTO implements Serializable {
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
    private String content;
}
