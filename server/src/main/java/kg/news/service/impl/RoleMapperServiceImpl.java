package kg.news.service.impl;

import kg.news.entity.RoleMapper;
import kg.news.repository.RoleMapperRepository;
import kg.news.service.RoleMapperService;
import org.springframework.stereotype.Service;

@Service
public class RoleMapperServiceImpl implements RoleMapperService {
    private final RoleMapperRepository roleMapperRepository;

    public RoleMapperServiceImpl(RoleMapperRepository roleMapperRepository) {
        this.roleMapperRepository = roleMapperRepository;
    }

    public void addRoleMapper(RoleMapper roleMapper) {
        roleMapperRepository.save(roleMapper);
    }
}
