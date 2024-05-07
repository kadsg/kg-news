package kg.news.repository;

import kg.news.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends CrudRepository<User, Long>, JpaRepository<User, Long> {
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户
     */
    User findByUsername(String username);

    /**
     * 根据用户ID列表查找用户
     * @param userIds 用户ID列表
     * @return 用户列表
     */
    List<User> findAllByIdIn(List<Long> userIds);
}
