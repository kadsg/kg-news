package kg.news.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 新闻概要视图对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsSummaryVO implements Serializable {
    /**
     * 新闻ID
     */
    private Long newsId;
    /**
     * 标签ID
     */
    private Long tagId;
    /**
     * 媒体ID
     */
    private Long mediaId;
    /**
     * 媒体名
     */
    private String mediaName;
    /**
     * 标题
     */
    private String title;
    /**
     * 封面
     */
    private String cover;
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
    private LocalDateTime postTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsSummaryVO that = (NewsSummaryVO) o;
        return this.newsId.equals(that.newsId);
    }
}
