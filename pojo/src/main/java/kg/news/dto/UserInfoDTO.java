package kg.news.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息修改DTO
 */
@Data
public class UserInfoDTO implements Serializable {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 用户描述
     */
    private String description;
}
