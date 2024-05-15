package kg.news.mapper;

import com.github.pagehelper.Page;
import kg.news.dto.FansQueryDTO;
import kg.news.dto.FollowQueryDTO;
import kg.news.vo.FansVO;
import kg.news.vo.FollowVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FollowMapper {
    /**
     * 查询粉丝列表
     * @param fansQueryDTO 查询条件
     * @return 粉丝列表
     */
    Page<FansVO> queryFansList(FansQueryDTO fansQueryDTO);

    /**
     * 查询关注列表
     * @param followQueryDTO 查询条件
     * @return 关注列表
     */
    Page<FollowVO> queryFollowList(FollowQueryDTO followQueryDTO);
}
