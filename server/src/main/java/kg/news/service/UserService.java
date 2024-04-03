package kg.news.service;

import kg.news.entity.User;

public interface UserService {
    /**
     * 查询用户
     * @param userId 用户id
     * @return 用户
     */
    User queryUserById(Long userId);
}
