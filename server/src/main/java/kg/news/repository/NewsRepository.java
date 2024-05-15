package kg.news.repository;

import kg.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
    /**
     * 根据标签ID统计新闻数量
     * @param tagId 标签ID
     * @return 新闻数量
     */
    long countByTagIdAndDeleteFlagIsFalse(Long tagId);

    /**
     * 根据创建者ID查找标签ID
     * @param userId 创建者ID
     * @return 标签ID列表
     */
    List<News> findByCreateUser(Long userId);
}
