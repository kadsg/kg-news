package kg.news.repository;

import kg.news.entity.RoleMapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMapperRepository extends CrudRepository<RoleMapper, Long>{

}
