package kg.news.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 新闻详情视图对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsDetailVO implements Serializable {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 媒体ID
     */
    private Long mediaId;
    /**
     * 媒体名
     */
    private String mediaName;
    /**
     * 标签ID
     */
    private Long tagId;
    /**
     * 标签名
     */
    private String tagName;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 浏览数
     */
    private int viewCount;
    /**
     * 评论数
     */
    private int commentCount;
    /**
     * 点赞数
     */
    private int likeCount;
    /**
     * 踩数
     */
    private int unlikeCount;
    /**
     * 发布时间
     */
    private LocalDateTime createTime;
}
