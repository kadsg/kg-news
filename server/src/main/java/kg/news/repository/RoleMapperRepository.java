package kg.news.repository;

import kg.news.entity.RoleMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleMapperRepository extends CrudRepository<RoleMapper, Long>{

    /**
     * 根据角色ID查询用户ID
     *
     * @param roleId   角色ID
     * @param pageable 分页信息
     * @return 用户ID列表
     */
    Page<RoleMapper> findUserIdsByRoleId(long roleId, Pageable pageable);
    /**
     * 根据用户ID查询角色映射信息
     * @param id 用户ID
     * @return 角色信息
     */
    RoleMapper findByUserId(long id);
}
