package kg.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 新闻标签数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsTagDTO implements Serializable {
    /**
     * 标签ID
     */
    private Long id;
    /**
     * 标签名
     */
    private String name;
    /**
     * 描述
     */
    private String description;
}
