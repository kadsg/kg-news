package kg.news.service.impl;

import kg.news.entity.Role;
import kg.news.dto.RoleDTO;
import kg.news.repository.RoleRepository;
import kg.news.service.RoleService;
import org.springframework.stereotype.Service;

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
}
