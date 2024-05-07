package kg.news.repository;

import kg.news.entity.NewsKeyWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsKeyWordRepository extends JpaRepository<NewsKeyWord, Long> {
    /**
     * 根据新闻id查找新闻关键词
     * @param newsId 新闻id
     * @return 新闻关键词
     */
    NewsKeyWord findByNewsId(Long newsId);
}
