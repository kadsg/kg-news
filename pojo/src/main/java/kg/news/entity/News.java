package kg.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class News implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long typeId;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    // 封面
    @Column(nullable = false)
    private String cover;
    private int viewCount;
    private int commentCount;
    private int likeCount;
    private int unlikeCount;
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime createTime;
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime updateTime;
    @Column(nullable = false)
    private Long createUser;
    @Column(nullable = false)
    private Long updateUser;
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0", insertable = false)
    private Boolean deleteFlag;
}
