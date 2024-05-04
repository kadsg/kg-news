package kg.news.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 新闻标签分页查询数据传输对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsTagQueryDTO extends PageRequestDTO implements Serializable {
    /**
     * 标签id
     */
    private Long tagId;
    /**
     * 标签名
     */
    private String tagName;
}
