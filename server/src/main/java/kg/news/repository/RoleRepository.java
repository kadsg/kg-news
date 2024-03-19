package kg.news.repository;

import kg.news.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    /**
     * 通过角色名查找角色
     * @param roleName 角色名
     * @return 角色
     */
    Role findByName(String roleName);
}
