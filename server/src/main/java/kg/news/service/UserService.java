package kg.news.service;

import kg.news.dto.UserInfoDTO;
import kg.news.dto.UserQueryDTO;
import kg.news.entity.User;
import kg.news.result.PageResult;
import kg.news.vo.UserVO;

import java.util.List;

public interface UserService {
    /**
     * 查询用户
     * @param userId 用户id
     * @return 用户
     */
    User queryUserById(Long userId);

    /**
     * 分页查询用户
     * @param userQueryDTO 用户查询条件
     * @return 用户列表
     */
    PageResult<UserVO> queryUserByRoleId(UserQueryDTO userQueryDTO);

    /**
     * 查询所有普通用户（不包含媒体、管理员）
     * @return 用户列表
     */
    List<User> queryAllUser();

    /**
     * 更新用户信息
     * @param userInfoDTO 用户信息
     */
    void updateUser(UserInfoDTO userInfoDTO);
}
