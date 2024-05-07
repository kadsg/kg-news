package kg.news.repository;

import kg.news.entity.Recommend;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendRepository extends JpaRepository<Recommend, Long>, CrudRepository<Recommend, Long> {
    /**
     * 根据用户id分页查询推荐新闻
     * @param userId 用户id
     * @param pageable 分页参数
     * @return 推荐新闻列表
     */
    List<Recommend> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据用户id和新闻id精确查询推荐新闻
     * @param userId 用户id
     * @param newsId 新闻id
     * @return 推荐新闻
     */
    Recommend findByUserIdAndNewsId(Long userId, Long newsId);

    /**
     * 根据用户id查询为其推荐的新闻中未读的新闻
     * @param userId 用户id
     * @param pageable 分页参数
     * @return 未读的推荐新闻列表
     */
    List<Recommend> findRecommendsByReadFlagIsFalseAndUserId(Long userId, Pageable pageable);

    /**
     * 根据用户id查询为其推荐的新闻中未读的新闻
     * @param userId 用户id
     * @return 未读的推荐新闻列表
     */
    List<Recommend> findRecommendsByReadFlagIsFalseAndUserId(Long userId);
}
