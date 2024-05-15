package kg.news.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 收藏查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FavoriteQueryDTO extends PageRequestDTO implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 新闻ID
     */
    private Long newsId;
    /**
     * 新闻标题
     */
    private String title;
    /**
     * 标签ID
     */
    private Long tagId;
}
