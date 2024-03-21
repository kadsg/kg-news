package kg.news.repository;

import kg.news.entity.TagSelection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagSelectionRepository extends CrudRepository<TagSelection, Long>{
    TagSelection findByUserId(Long userId);
}
