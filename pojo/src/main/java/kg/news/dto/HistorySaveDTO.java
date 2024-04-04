package kg.news.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 浏览历史数据传输对象（保存）
 */
@Data
public class HistorySaveDTO implements Serializable {
    /**
     * 用户id
     */
    private Long mediaId;
    /**
     * 媒体名称
     */
    private String mediaName;
    /**
     * 标签id
     */
    private Long tagId;
    /**
     * 标签名
     */
    private String tagName;
    /**
     * 新闻id
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
}
