package kg.news.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户数据列表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVO implements Serializable {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 描述
     */
    private String description;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * 是否启用
     */
    private Boolean status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
