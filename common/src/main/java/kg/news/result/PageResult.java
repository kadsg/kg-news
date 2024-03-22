package kg.news.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 */
@Data
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    /**
     * 总数
     */
    private Long total;
    /**
     * 当前页数据
     */
    private List<T> records;
}
