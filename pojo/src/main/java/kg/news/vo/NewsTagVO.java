package kg.news.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 新闻标签视图对象
 */
@Data
@Builder
public class NewsTagVO implements Serializable {
    /**
     * 标签id
     */
    private Long tagId;
    /**
     * 标签名
     */
    private String tagName;
    /**
     * 描述
     */
    private String description;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 创建用户id
     */
    private Long createUserId;
    /**
     * 更新用户id
     */
    private Long updateUserId;
    /**
     * 新闻数量统计
     */
    private Long count;
    /**
     * 删除标记
     */
    private Boolean deleteFlag;
}
