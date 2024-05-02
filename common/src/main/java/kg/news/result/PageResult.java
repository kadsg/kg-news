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
     * 页码
     */
    int pageNum;
    /**
     * 每页大小
     */
    int pageSize;
    /**
     * 总记录数
     */
    long total;
    /**
     * 数据
     */
    private List<T> list;
}
