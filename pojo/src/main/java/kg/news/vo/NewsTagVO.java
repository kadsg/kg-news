package kg.news.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 新闻标签视图对象
 */
@Data
@Builder
public class NewsTagVO implements Serializable {
    /**
     * 标签列表
     */
    private List<Map<Long, String>> tags;
}
