package kg.news.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录数据
 */
@Data
public class LoginDTO implements Serializable {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
