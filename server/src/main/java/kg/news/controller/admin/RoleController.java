package kg.news.controller.admin;

import kg.news.dto.RoleDTO;
import kg.news.result.Result;
import kg.news.service.RoleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/admin")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 添加角色
     * @param roleDTO 角色信息
     * @return 添加结果
     */
    @PostMapping("/addRole")
    public Result<Object> addRole(@RequestBody RoleDTO roleDTO) {
        roleService.addRole(roleDTO);
        return Result.success();
    }
}
