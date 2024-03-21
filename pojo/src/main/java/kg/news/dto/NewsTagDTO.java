package kg.news.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class NewsTagDTO implements Serializable {
    private String name;
    private String description;
}
