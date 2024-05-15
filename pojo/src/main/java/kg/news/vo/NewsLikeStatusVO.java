package kg.news.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 新闻点赞状态视图对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsLikeStatusVO implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 新闻ID
     */
    private Long newsId;
    /**
     * 点赞状态
     */
    private Boolean likeStatus;
    /**
     * 点踩状态
     */
    private Boolean dislikeStatus;
}
