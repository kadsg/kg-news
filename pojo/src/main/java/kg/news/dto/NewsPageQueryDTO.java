package kg.news.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 新闻分页查询数据传输对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsPageQueryDTO extends PageRequestDTO implements Serializable {
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
}
