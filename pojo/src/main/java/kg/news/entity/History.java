package kg.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

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
    /**
     * 标签ID
     */
    @Column(nullable = false)
    private Long tagId;
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
     * 删除标记
     */
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0", insertable = false)
    private Boolean deleteFlag;
}
