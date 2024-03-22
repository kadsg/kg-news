package kg.news.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色数据
 */
@Data
public class RoleDTO implements Serializable {
    /**
     * 角色名
     */
    private String name;
    /**
     * 角色代码
     */
    private String code;
    /**
     * 描述
     */
    private String description;
}
