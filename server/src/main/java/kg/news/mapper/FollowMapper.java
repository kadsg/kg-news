package kg.news.mapper;

import com.github.pagehelper.Page;
import kg.news.dto.FansQueryDTO;
import kg.news.vo.FansVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FollowMapper {
    /**
     * 查询粉丝列表
     * @param fansQueryDTO 查询条件
     * @return 粉丝列表
     */
    Page<FansVO> queryFansList(FansQueryDTO fansQueryDTO);
}
