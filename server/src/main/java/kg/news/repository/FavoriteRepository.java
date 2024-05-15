package kg.news.repository;

import kg.news.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends CrudRepository<Favorite, Long>, JpaRepository<Favorite, Long> {
    /**
     * 根据新闻ID和用户ID查询收藏
     * @param newsId 新闻ID
     * @param userId 用户ID
     * @return 收藏
     */
    Favorite findByNewsIdAndUserId(Long newsId, Long userId);

    /**
     * 根据用户ID查询收藏
     * @param userId 用户ID
     * @return 收藏列表
     */
    List<Favorite> findByUserId(Long userId);
}
