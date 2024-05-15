package kg.news.mapper;

import com.github.pagehelper.Page;
import kg.news.dto.FavoriteQueryDTO;
import kg.news.vo.NewsSummaryVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteMapper {
    /**
     * 查询收藏新闻
     * @param favoriteQueryDTO 查询条件
     * @return 收藏新闻列表
     */
    Page<NewsSummaryVO> queryFavoriteNews(FavoriteQueryDTO favoriteQueryDTO);
}
