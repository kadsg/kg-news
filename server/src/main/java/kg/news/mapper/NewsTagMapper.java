package kg.news.mapper;

import com.github.pagehelper.Page;
import kg.news.dto.NewsTagQueryDTO;
import kg.news.entity.NewsTag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsTagMapper {
    /**
     * 查询新闻
     * @param newsTagQueryDTO 查询条件
     * @return 新闻列表
     */
    Page<NewsTag> queryNewsTag(NewsTagQueryDTO newsTagQueryDTO);
}
