package kg.news.service.impl;

import com.github.pagehelper.PageHelper;
import kg.news.context.BaseContext;
import kg.news.dto.FansQueryDTO;
import kg.news.dto.FollowQueryDTO;
import kg.news.entity.Follow;
import kg.news.entity.UserFollowStatus;
import kg.news.mapper.FollowMapper;
import kg.news.repository.FollowRepository;
import kg.news.repository.UserFollowStatusRepository;
import kg.news.result.PageResult;
import kg.news.service.FollowService;
import kg.news.service.UserService;
import kg.news.vo.FansVO;
import kg.news.vo.FollowVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;
    private final UserFollowStatusRepository userFollowStatusRepository;
    private final FollowMapper followMapper;

    public FollowServiceImpl(FollowRepository followRepository, UserService userService,
                             UserFollowStatusRepository userFollowStatusRepository, FollowMapper followMapper) {
        this.followRepository = followRepository;
        this.userService = userService;
        this.userFollowStatusRepository = userFollowStatusRepository;
        this.followMapper = followMapper;
    }

    @Transactional
    public void follow(Long followId) {
        Long userId = BaseContext.getCurrentId();
        Follow follow = followRepository.findByUserIdAndFollowUserId(userId, followId);
        // 执行用户
        UserFollowStatus userFollowStatus = userFollowStatusRepository.findByUserId(userId);
        // 被关注用户
        UserFollowStatus followStatus = userFollowStatusRepository.findByUserId(followId);
        // 如果有记录
        if (follow != null) {
            // 如果是取消关注状态，则改为关注状态
            if (follow.getDeleteFlag()) {
                follow.setDeleteFlag(false);
                // 更改双方的关注数
                userFollowStatus.setFollowCount(userFollowStatus.getFollowCount() + 1);
                followStatus.setFansCount(followStatus.getFansCount() + 1);
            } else {
                // 如果是关注状态，则改为取消关注状态
                follow.setDeleteFlag(true);
                // 更改双方的关注数
                userFollowStatus.setFollowCount(userFollowStatus.getFollowCount() - 1);
                followStatus.setFansCount(followStatus.getFansCount() - 1);
            }
        } else {
            // 如果没有记录，则新增
            follow = Follow.builder().userId(userId).followUserId(followId).deleteFlag(false).build();
        }
        followRepository.save(follow);
        userFollowStatusRepository.save(userFollowStatus);
        userFollowStatusRepository.save(followStatus);
    }

    public PageResult<FollowVO> queryFollowList(FollowQueryDTO followQueryDTO) {
        int page = followQueryDTO.getPageNum();
        int pageSize = followQueryDTO.getPageSize();
        Long userId = followQueryDTO.getUserId();
        // 如果page或pageSize非法，则设置默认值
        if (page <= 0 || pageSize <= 0) {
            page = 1;
            pageSize = 10;
        }
        // 如果userId为空，则取当前用户ID
        if (userId == null) {
            userId = BaseContext.getCurrentId();
        }
        // 查询关注列表
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<Follow> followPage = followRepository.findAllByUserId(userId, pageRequest);
        List<Follow> followList = followPage.getContent();
        // 转换为VO
        List<FollowVO> followVOList = followList
                .stream()
                .map(follow ->
                        FollowVO.builder()
                                .followId(follow.getFollowUserId())
                                .followName(userService.queryUserById(follow.getFollowUserId()).getNickname())
                                .build())
                .toList();
        return new PageResult<>(page, pageSize, followPage.getTotalElements(), followVOList);
    }

    public PageResult<FansVO> queryFansList(FansQueryDTO fansQueryDTO) {
        int page = fansQueryDTO.getPageNum();
        int pageSize = fansQueryDTO.getPageSize();
        Long userId = fansQueryDTO.getUserId();

        // 如果page或pageSize非法，则设置默认值
        if (page <= 0 || pageSize <= 0) {
            page = 1;
            pageSize = 10;
        }
        // 如果userId为空，则取当前用户ID
        if (userId == null) {
            userId = BaseContext.getCurrentId();
            fansQueryDTO.setUserId(userId);
        }
        // 查询关注列表
        PageHelper.startPage(page, pageSize);
        com.github.pagehelper.Page<FansVO> followPage = followMapper.queryFansList(fansQueryDTO);
        return new PageResult<>(followPage.getPageNum(), followPage.getPageSize(), followPage.getTotal(), followPage.getResult());
    }
}
