package kg.news.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class LoginVO implements Serializable {
    // 主键值
    private Long id;
    private String token;
    // 拓展字段
    private String code;
}
