package kg.news.entity;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 关注表
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Follow implements Serializable {
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
     * 被关注用户ID
     */
    @Column(nullable = false)
    private Long followUserId;
    /**
     * 删除标记
     */
    @Column(nullable = false)
    private Boolean deleteFlag;
}
