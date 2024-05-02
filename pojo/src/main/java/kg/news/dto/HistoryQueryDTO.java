package kg.news.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 浏览历史数据传输对象（查询）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryQueryDTO  extends PageRequestDTO implements Serializable {
    /**
     * 用户id
     */
    private Long userId;
}
