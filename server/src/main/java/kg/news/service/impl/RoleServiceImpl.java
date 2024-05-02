package kg.news.service.impl;

import kg.news.entity.Role;
import kg.news.dto.RoleDTO;
import kg.news.repository.RoleRepository;
import kg.news.result.PageResult;
import kg.news.service.RoleService;
import kg.news.vo.RoleVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void addRole(RoleDTO roleDTO) {
        Role role = Role.builder()
            .name(roleDTO.getName())
            .code(roleDTO.getCode())
            .description(roleDTO.getDescription())
            .build();
        roleRepository.save(role);
    }

    public Role getRole(String roleName) {
        return roleRepository.findByName(roleName);
    }

    public PageResult<RoleVO> queryRoles() {
        Iterable<Role> iterable = roleRepository.findAll();
        List<Role> roles = (List<Role>) iterable;
        List<RoleVO> list = roles.stream().map(role -> RoleVO.builder()
                .roleId(role.getId())
                .roleName(role.getName())
                .description(role.getDescription())
                .createTime(role.getCreateTime())
                .build()).toList();

        return new PageResult<>(1, 10, list.size(), list);
    }
}
