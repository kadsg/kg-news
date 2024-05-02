package kg.news.service;

import kg.news.dto.LoginDTO;
import kg.news.entity.User;
import kg.news.vo.LoginVO;

public interface LoginService {
    /**
     * 登录
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 获取用户令牌
     * @param admin 用户信息
     * @return 令牌
     */
    String getToken(User admin);

    /**
     * 注册
     * @param loginDTO 注册信息
     * @return 用户信息
     */
    User register(LoginDTO loginDTO);
}
