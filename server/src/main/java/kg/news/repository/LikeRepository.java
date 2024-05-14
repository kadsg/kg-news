package kg.news.repository;

import kg.news.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<CommentLike, Long>, CrudRepository<CommentLike, Long> {
    CommentLike findByCommentIdAndUserId(Long commentId, Long userId);
}
