package kg.news.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询DTO
 */
@Data
public class FansQueryDTO implements Serializable {
    /**
     * 用户ID
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
