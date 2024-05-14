package kg.news.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 分页查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FansQueryDTO  extends PageRequestDTO implements Serializable {
    /**
     * 所属用户ID
     */
    private Long userId;
    /**
     * 粉丝ID
     */
    private Long fansId;
    /**
     * 昵称
     */
    private String nickname;
}
