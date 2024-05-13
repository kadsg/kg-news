package kg.news.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryDTO  extends PageRequestDTO implements Serializable {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
}
