package kg.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserFollowStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 用户ID
     */
    @Column(nullable = false)
    private Long userId;
    /**
     * 关注人数
     */
    @Column(nullable = false)
    private Integer followCount;
    /**
     * 粉丝人数
     */
    @Column(nullable = false)
    private Integer fansCount;
}
