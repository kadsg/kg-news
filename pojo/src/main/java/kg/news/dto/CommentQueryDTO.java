package kg.news.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 评论查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentQueryDTO extends PageRequestDTO implements Serializable {
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
}
