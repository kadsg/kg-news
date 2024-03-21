package kg.news.repository;

import kg.news.entity.NewsTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsTagRepository extends CrudRepository<NewsTag, Long>{
}
