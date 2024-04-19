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
    private Long followId;
    /**
     * 被关注者昵称
     */
    private String followName;
}
