package kg.news.repository;

import kg.news.entity.UserFollowStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFollowStatusRepository extends CrudRepository<UserFollowStatus, Long> {
    /**
     * 根据用户ID查询关注状态
     * @param followId 用户ID
     * @return 关注状态
     */
    UserFollowStatus findByUserId(Long followId);
}
