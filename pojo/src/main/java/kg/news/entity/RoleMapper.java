package kg.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户权限映射表
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RoleMapper implements Serializable {
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
     * 角色ID
     */
    @Column(nullable = false)
    private Long roleId;
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
    private Long createUser;
    /**
     * 更新用户
     */
    private Long updateUser;
    /**
     * 删除标记
     */
    @Column(nullable = false, insertable = false)
    private Boolean deleteFlag;
}
