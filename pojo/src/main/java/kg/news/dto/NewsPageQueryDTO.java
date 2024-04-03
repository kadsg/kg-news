package kg.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 新闻分页查询数据传输对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsPageQueryDTO implements Serializable {
    /**
     * 新闻id
     */
    private Long newsId;
    /**
     * 媒体id
     */
    private Long mediaId;
    /**
     * 新闻类型id
     */
    private Long newsTagId;
    /**
     * 标题
     */
    private String title;
    /**
     * 页码
     */
    private int page;
    /**
     * 每页大小
     */
    private int pageSize;
}
