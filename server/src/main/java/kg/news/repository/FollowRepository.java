package kg.news.repository;

import kg.news.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

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
     * @param pageable 分页请求
     * @return 分页关注列表
     */
    Page<Follow> findAllByUserId(Long userId, Pageable pageable);

    /**
     * 根据被关注用户ID查询粉丝列表
     * @param userId 被关注用户ID
     * @param pageable 分页请求
     * @return 粉丝列表
     */
    Page<Follow> findAllByFollowUserId(Long userId, Pageable pageable);
}
