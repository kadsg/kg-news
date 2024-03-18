package kg.news.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kg.oss")
@Data
public class OssProperties {
    private String domain;
    private String accessKey;
    private String secretKey;
    private String bucket;
}
