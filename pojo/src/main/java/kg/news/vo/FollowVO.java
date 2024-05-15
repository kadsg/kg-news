package kg.news.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 关注视图
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowVO implements Serializable {
    /**
     * 被关注者ID
     */
    private Long id;
    /**
     * 被关注者昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 描述
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
