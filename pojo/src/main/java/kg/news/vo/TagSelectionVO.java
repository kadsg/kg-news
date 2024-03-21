package kg.news.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
public class TagSelectionVO implements Serializable {
    private Long userId;
    private Set<Long> tags;
}
