package kg.news.service.impl;

import kg.news.constant.RoleConstant;
import kg.news.dto.UserQueryDTO;
import kg.news.entity.Role;
import kg.news.entity.RoleMapper;
import kg.news.entity.User;
import kg.news.exception.RoleException;
import kg.news.repository.RoleMapperRepository;
import kg.news.repository.RoleRepository;
import kg.news.repository.UserRepository;
import kg.news.result.PageResult;
import kg.news.service.UserService;
import kg.news.vo.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleMapperRepository roleMapperRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleMapperRepository roleMapperRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleMapperRepository = roleMapperRepository;
        this.roleRepository = roleRepository;
    }

    public User queryUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public PageResult<UserVO> queryUserByRoleId(UserQueryDTO userQueryDTO) {
        List<UserVO> list;
        int page = userQueryDTO.getPageNum();
        int pageSize = userQueryDTO.getPageSize();
        if (page <= 0 || pageSize <= 0) {
            // 页码和每页大小必须大于0，未指定则默认为1和10
            page = 1;
            pageSize = 10;
        }
        // JPA分页查询，页码从0开始
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        // 不指定角色ID则查询所有用户
        if (userQueryDTO.getRoleId() == null || 0 == userQueryDTO.getRoleId()) {
            // 分页查询所有用户
            Page<User> userPage = userRepository.findAll(pageRequest);
            List<User> userList = userPage.toList();
            list = userList.stream().map(user -> {
                // 根据用户ID查询角色映射信息
                RoleMapper roleMapper = roleMapperRepository.findByUserId(user.getId());
                // 根据角色ID查询角色信息
                Role role = roleRepository.findById(roleMapper.getRoleId()).orElse(null);
                assert role != null;
                return UserVO.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .roleId(role.getId())
                        .roleName(role.getName())
                        .nickname(user.getNickname())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .createUser(user.getCreateUser())
                        .status(user.getEnabled())
                        .createTime(user.getCreateTime())
                        .updateTime(user.getUpdateTime())
                        .build();
            }).toList();
            return new PageResult<>(page, pageSize, userRepository.count(), list);
        } else {
            // 根据角色ID查询角色信息
            // 如果角色不存在则抛出异常
            Role role = roleRepository.findById(userQueryDTO.getRoleId()).orElse(null);
            if (role == null) {
                throw new RoleException(RoleConstant.ROLE_NOT_FOUND);
            }
            // 根据角色ID分页查询用户ID
            Page<RoleMapper> roleMapperPage = roleMapperRepository.findUserIdsByRoleId(userQueryDTO.getRoleId(), pageRequest);
            // 用户ID列表
            List<Long> userIds = roleMapperPage.stream().map(RoleMapper::getUserId).toList();
            // 根据用户ID查询用户信息
            List<User> userList = userRepository.findAllById(userIds);
            list = userList.stream().map(user -> UserVO.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .roleId(userQueryDTO.getRoleId())
                    .roleName(role.getName())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .createUser(user.getCreateUser())
                    .status(user.getEnabled())
                    .createTime(user.getCreateTime())
                    .updateTime(user.getUpdateTime())
                    .build()).toList();
            return new PageResult<>(page, pageSize, roleMapperPage.getTotalElements(), list);
        }
    }
}
