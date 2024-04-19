package kg.news.service;

import kg.news.dto.FansQueryDTO;
import kg.news.dto.FollowQueryDTO;
import kg.news.result.PageResult;
import kg.news.vo.FansVO;
import kg.news.vo.FollowVO;

public interface FollowService {
    /**
     * 关注或取消关注
     * @param followId 被关注的账号ID
     */
    void follow(Long followId);

    /**
     * 查询关注列表
     * @param followQueryDTO 查询条件
     * @return 关注列表
     */
    PageResult<FollowVO> queryFollowList(FollowQueryDTO followQueryDTO);

    /**
     * 查询粉丝列表
     * @param fansQueryDTO 查询条件
     * @return 粉丝列表
     */
    PageResult<FansVO> queryFansList(FansQueryDTO fansQueryDTO);
}
