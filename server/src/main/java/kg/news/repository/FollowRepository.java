package kg.news.repository;

import kg.news.entity.Follow;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long>, CrudRepository<Follow, Long> {
    /**
     * 根据用户ID和被关注用户ID查询关注记录
     * @param userId 用户ID
     * @param followId 被关注用户ID
     * @return 关注记录
     */
    Follow findByUserIdAndFollowUserId(Long userId, Long followId);

    /**
     * 根据用户ID查询关注列表
     * @param userId 用户ID
     * @param pageRequest 分页请求
     * @return 关注列表
     */
    List<Follow> findAllByUserId(Long userId, PageRequest pageRequest);

    /**
     * 根据被关注用户ID查询粉丝列表
     * @param userId 被关注用户ID
     * @param pageRequest 分页请求
     * @return 粉丝列表
     */
    List<Follow> findAllByFollowUserId(Long userId, PageRequest pageRequest);
}
