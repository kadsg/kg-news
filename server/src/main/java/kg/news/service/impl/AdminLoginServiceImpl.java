package kg.news.service.impl;

import kg.news.dto.LoginDTO;
import kg.news.entity.RoleMapper;
import kg.news.entity.User;
import kg.news.enumration.OperationType;
import kg.news.properties.JwtProperties;
import kg.news.repository.UserFollowStatusRepository;
import kg.news.repository.UserRepository;
import kg.news.service.LoginService;
import kg.news.service.RoleMapperService;
import kg.news.service.RoleService;
import kg.news.utils.ServiceUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;

@Service("adminLoginServiceImpl")
public class AdminLoginServiceImpl extends LoginServiceImpl implements LoginService {

    public AdminLoginServiceImpl(UserRepository userRepository, JwtProperties jwtProperties, RoleService roleService, RoleMapperService roleMapperService, UserFollowStatusRepository userFollowStatusRepository) {
        super(userRepository, jwtProperties, roleService, roleMapperService, userFollowStatusRepository);
    }

    /**
     * 管理员注册
     * @param loginDTO 注册信息
     * @return 用户实体
     */
    @Transactional
    public User register(LoginDTO loginDTO) {
        User user = super.register(loginDTO);
        user = userRepository.save(user);
        Long roleId = roleService.getRole("admin").getId();
        RoleMapper roleMapper = RoleMapper.builder()
                .userId(user.getId())
                .roleId(roleId)
                .build();
        try {
            ServiceUtil.autoFill(roleMapper, OperationType.INSERT);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        roleMapperService.addRoleMapper(roleMapper);
        return user;
    }
}