package kg.news.repository;

import kg.news.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    /**
     * 分页查询用户浏览历史记录
     *
     * @param userId 用户ID
     * @param pageable 分页信息
     * @return 浏览历史记录
     */
    Page<History> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据用户ID和新闻ID查询浏览历史记录
     *
     * @param userId 用户ID
     * @param newsId 新闻ID
     * @return 浏览历史记录
     */
    History findByUserIdAndNewsId(Long userId, Long newsId);
}
