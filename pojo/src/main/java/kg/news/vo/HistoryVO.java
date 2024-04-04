package kg.news.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 浏览历史数据传输对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryVO implements Serializable {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 发布媒体ID
     */
    private Long mediaId;
    /**
     * 发布媒体名称
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
     * 新闻ID
     */
    private Long newsId;
    /**
     * 标题
     */
    private String title;
    /**
     * 封面
     */
    private String cover;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 删除标记
     */
    private Boolean deleted;
}
