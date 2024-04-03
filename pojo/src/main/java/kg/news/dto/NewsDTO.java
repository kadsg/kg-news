package kg.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 新闻发布DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsDTO implements Serializable {
    /**
     * 所属标签ID
     */
    private Long tagId;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 封面
     */
    private String cover;
}
