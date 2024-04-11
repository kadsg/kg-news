package kg.news.repository;

import kg.news.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends CrudRepository<Favorite, Long>, JpaRepository<Favorite, Long> {
}
