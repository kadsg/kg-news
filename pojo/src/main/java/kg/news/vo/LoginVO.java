package kg.news.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录数据
 */
@Builder
@Data
public class LoginVO implements Serializable {
    /**
     * 用户id
     */
    private Long id;
    /**
     * token
     */
    private String token;
    /**
     * 过期时间戳
     */
    private Long expires;
    /**
     * 拓展字段
     */
    private String code;
}
