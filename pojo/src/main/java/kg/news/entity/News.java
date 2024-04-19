package kg.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 新闻表
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class News implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 标签ID
     */
    @Column(nullable = false)
    private Long tagId;
    /**
     * 标题
     */
    @Column(nullable = false)
    private String title;
    /**
     * 内容
     */
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    /**
     * 封面
     */
    @Column(nullable = false)
    private String cover;
    /**
     * 浏览数
     */
    private int viewCount;
    /**
     * 评论数
     */
    private int commentCount;
    /**
     * 点赞数
     */
    private int likeCount;
    /**
     * 踩数
     */
    private int unlikeCount;
    /**
     * 创建时间
     */
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime updateTime;
    /**
     * 创建用户
     */
    @Column(nullable = false)
    private Long createUser;
    /**
     * 更新用户
     */
    @Column(nullable = false)
    private Long updateUser;
    /**
     * 删除标记
     */
    @Column(nullable = false, insertable = false)
    private Boolean deleteFlag;
}
