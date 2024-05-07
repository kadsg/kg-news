package kg.news.mapper;

import com.github.pagehelper.Page;
import kg.news.dto.NewsPageQueryDTO;
import kg.news.vo.NewsSummaryVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsMapper {
    /**
     * 分页查询新闻
     * @param newsPageQueryDTO 分页查询条件
     * @return 新闻列表
     */
    Page<NewsSummaryVO> queryNews(NewsPageQueryDTO newsPageQueryDTO);

    /**
     * 查询浏览数最多的新闻
     * @return 新闻列表
     */
    Page<NewsSummaryVO> findTopByViewCount();

    /**
     * 查询点赞数最多的新闻
     * @return 新闻列表
     */
    Page<NewsSummaryVO> findTopByLikeCount();

    /**
     * 查询评论数最多的新闻
     * @return 新闻列表
     */
    Page<NewsSummaryVO> findTopByCommentCount();
}
