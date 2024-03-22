package kg.news.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 新闻标签数据
 */
@Data
@Builder
public class NewsTagDTO implements Serializable {
    /**
     * 标签名
     */
    private String name;
    /**
     * 描述
     */
    private String description;
}
