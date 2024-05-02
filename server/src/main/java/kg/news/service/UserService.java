package kg.news.service;

import kg.news.dto.UserQueryDTO;
import kg.news.entity.User;
import kg.news.result.PageResult;
import kg.news.vo.UserVO;

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
}
