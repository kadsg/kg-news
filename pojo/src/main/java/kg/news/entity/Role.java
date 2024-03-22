package kg.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统用户角色表
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 角色名称
     */
    @Column(nullable = false, unique = true)
    private String name;
    /**
     * 角色编码
     */
    @Column(nullable = false, unique = true)
    private String code;
    /**
     * 描述
     */
    private String description;
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
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0", insertable = false)
    private Boolean deleteFlag;
}
