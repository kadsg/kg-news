package kg.news.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 标签选择数据
 */
@Data
@Builder
public class TagSelectionVO implements Serializable {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 标签id
     */
    private Set<Long> tags;
}
