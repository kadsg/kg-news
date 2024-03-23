package kg.news.repository;

import kg.news.entity.NewsTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsTagRepository extends CrudRepository<NewsTag, Long>{
    /**
     * 通过名称查找新闻标签
     * @param name 新闻标签名称
     * @return 新闻标签
     */
    NewsTag findByName(String name);
}
