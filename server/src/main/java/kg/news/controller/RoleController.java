package kg.news.controller;

import kg.news.dto.RoleDTO;
import kg.news.result.PageResult;
import kg.news.result.Result;
import kg.news.service.RoleService;
import kg.news.vo.RoleVO;
import org.springframework.web.bind.annotation.*;

/**
 * 角色控制器
 */
@CrossOrigin
@RestController
@RequestMapping("/role")
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
    @PostMapping
    public Result<Object> addRole(@RequestBody RoleDTO roleDTO) {
        roleService.addRole(roleDTO);
        return Result.success();
    }

    /**
     * 获取角色列表
     * @return 角色列表
     */
    @GetMapping("/list")
    public Result<PageResult<RoleVO>> queryRole() {
        PageResult<RoleVO> result = roleService.queryRoles();
        return Result.success(result);
    }
}
