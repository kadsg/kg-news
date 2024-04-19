package kg.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 历史记录表
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class History implements Serializable {
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
    private Long mediaId;
    private String mediaName;
    /**
     * 标签ID
     */
    @Column(nullable = false)
    private Long tagId;
    /**
     * 标签名
     */
    private String tagName;
    /**
     * 新闻ID
     */
    @Column(nullable = false)
    private Long newsId;
    /**
     * 标题
     */
    @Column(nullable = false)
    private String title;
    /**
     * 封面
     */
    @Column(nullable = false)
    private String cover;
    /**
     * 更新时间
     */
    @Column(nullable = false)
    private LocalDateTime updateTime;
    /**
     * 删除标记
     */
    @Column(nullable = false, insertable = false)
    private Boolean deleteFlag;
}