package kg.news.entity;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通用通知
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notice implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 发送者姓名
     */
    private String senderName;
    /**
     * 标题
     */
    @Column(nullable = false)
    private String title;
    /**
     * 内容
     */
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;
    /**
     * 阅读标记
     */
    @Column(nullable = false)
    private Boolean readFlag;
    /**
     * 创建时间
     */
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime updateTime;
    /**
     * 创建用户
     */
    @Column(nullable = false)
    private Long createUser;
    /**
     * 更新用户
     */
    @Column(nullable = false)
    private Long updateUser;
    /**
     * 删除标记
     */
    @Column(nullable = false, insertable = false)
    private Boolean deleteFlag;
}
