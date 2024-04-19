package kg.news.service.impl;

import kg.news.constant.LoginConstant;
import kg.news.constant.StatusConstant;
import kg.news.dto.LoginDTO;
import kg.news.entity.User;
import kg.news.exception.LoginException;
import kg.news.properties.JwtProperties;
import kg.news.repository.UserFollowStatusRepository;
import kg.news.repository.UserRepository;
import kg.news.service.LoginService;
import kg.news.service.RoleService;
import kg.news.service.RoleMapperService;
import kg.news.utils.JwtUtil;
import kg.news.utils.ServiceUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    final UserRepository userRepository;
    final JwtProperties jwtProperties;
    final RoleService roleService;
    final RoleMapperService roleMapperService;
    final UserFollowStatusRepository userFollowStatusRepository;

    public LoginServiceImpl(UserRepository userRepository, JwtProperties jwtProperties, RoleService roleService, RoleMapperService roleMapperService, UserFollowStatusRepository userFollowStatusRepository) {
        this.userRepository = userRepository;
        this.jwtProperties = jwtProperties;
        this.roleService = roleService;
        this.roleMapperService = roleMapperService;
        this.userFollowStatusRepository = userFollowStatusRepository;
    }

    public User login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new LoginException(LoginConstant.ACCOUNT_NOT_FOUND);
        }
        if (!user.getPassword().equals(ServiceUtil.encryptPassword(password))) {
            throw new LoginException(LoginConstant.PASSWORD_ERROR);
        }
        if (user.getEnabled() == StatusConstant.DIS_ENABLED) {
            throw new LoginException(LoginConstant.ACCOUNT_LOCKED);
        }
        return user;
    }

    public String getToken(User user) {
        Map<String, Long> claims = new HashMap<>();
        claims.put("id", user.getId());
        return JwtUtil.generateToken(jwtProperties.getSecretKey(), jwtProperties.getTtl(), claims);
    }

    public User register(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername());
        if (user != null) {
            throw new LoginException(LoginConstant.ACCOUNT_EXIST);
        }
        user = new User();
        user.setUsername(loginDTO.getUsername());
        user.setPassword(ServiceUtil.encryptPassword(loginDTO.getPassword()));
        return user;
    }
}
