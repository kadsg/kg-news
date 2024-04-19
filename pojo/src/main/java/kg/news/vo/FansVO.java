package kg.news.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 粉丝视图
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FansVO implements Serializable {
    /**
     * 粉丝ID
     */
    private Long fansId;
    /**
     * 粉丝昵称
     */
    private String fansName;
}
