package kg.news.service;

import kg.news.dto.RoleDTO;
import kg.news.entity.Role;

public interface RoleService {
    /**
     * 添加系统角色
     * @param roleDTO 角色信息
     */
    void addRole(RoleDTO roleDTO);

    /**
     * 根据角色名获取角色id
     * @param roleName 角色名
     * @return 角色id
     */
    Role getRole(String roleName);
}
