package kg.news.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 分页查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FollowQueryDTO  extends PageRequestDTO implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 被关注的账号ID
     */
    private Long followId;
    /**
     * 被关注人的昵称
     */
    private String nickname;
}
