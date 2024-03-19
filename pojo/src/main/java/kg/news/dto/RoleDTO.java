package kg.news.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleDTO implements Serializable {
    private String name;
    private String code;
    private String description;
}
