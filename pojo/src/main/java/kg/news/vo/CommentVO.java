package kg.news.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO implements Serializable {
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
    private Long fatherId;
    /**
     * 回复人ID
     */
    private Long replyId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论人
     */
    private String authorName;
    /**
     * 评论人ID
     */
    private Long authorId;
    /**
     * 点赞数
     */
    private int likeCount;
    /**
     * 点踩数
     */
    private int unlikeCount;
    /**
     * 评论时间
     */
    private LocalDateTime createTime;
}
