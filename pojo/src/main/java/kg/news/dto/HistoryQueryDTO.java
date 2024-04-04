package kg.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 浏览历史数据传输对象（查询）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryQueryDTO implements Serializable {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 页码
     */
    private int page;
    /**
     * 每页大小
     */
    private int pageSize;
}
