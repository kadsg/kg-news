package kg.news.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统角色列表
 */
@Data
@Builder
public class RoleVO implements Serializable {
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色描述
     */
    private String description;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
