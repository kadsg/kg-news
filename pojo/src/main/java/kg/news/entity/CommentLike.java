package kg.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 收藏记录表（评论点赞）
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CommentLike implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 用户ID
     */
    @Column(nullable = false)
    private Long userId;
    /**
     * 评论ID
     */
    @Column(nullable = false)
    private Long commentId;
    /**
     * 是否点赞
     */
    @Column(nullable = false)
    private boolean likeFlag;
    /**
     * 是否”踩“
     */
    @Column(nullable = false)
    private boolean dislikeFlag;
    /**
     * 是否删除
     */
    @Column(nullable = false)
    private boolean deleteFlag;
}
