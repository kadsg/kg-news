package kg.news.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 粉丝视图
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FansVO implements Serializable {
    /**
     * 粉丝ID
     */
    private Long id;
    /**
     * 粉丝昵称
     */
    private String nickname;
    /**
     * 粉丝头像
     */
    private String avatar;
    /**
     * 粉丝描述
     */
    private String description;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 删除标记
     */
    private Boolean deleteFlag;
}
