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
}
