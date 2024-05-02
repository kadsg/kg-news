package kg.news.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询请求
 */
@Data
public class PageRequestDTO implements Serializable {
    /**
     * 页码
     */
    int pageNum;
    /**
     * 每页大小
     */
    int pageSize;
}
