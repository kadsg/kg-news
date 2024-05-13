package kg.news.repository;

import kg.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
    /**
     * 根据标签ID统计新闻数量
     * @param tagId 标签ID
     * @return 新闻数量
     */
    long countByTagId(Long tagId);
}
