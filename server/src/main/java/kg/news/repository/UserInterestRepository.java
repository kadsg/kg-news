package kg.news.repository;

import kg.news.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long>, CrudRepository<UserInterest, Long> {
    /**
     * 根据用户ID查询用户兴趣
     *
     * @param userId 用户ID
     * @return 用户兴趣
     */
    UserInterest findByUserId(Long userId);
}
