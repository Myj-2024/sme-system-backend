package com.sme.controller;

import com.sme.dto.RolePermissionDTO;
import com.sme.entity.SysPermission;
import com.sme.entity.Role;
import com.sme.result.Result;
import com.sme.service.RolePermissionService;
import com.sme.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/role-permission")
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private RoleService roleService;

    /**
     * 1. 给角色分配菜单权限
     * 请求示例：{"roleId":3,"permissionIds":[9,5]}
     */
    @PostMapping("/assign")
    public Result<Void> assignPermission(@RequestBody RolePermissionDTO dto) {
        log.info("角色权限分配：{}", dto);
        return rolePermissionService.assignPermission(dto);
    }

    /**
     * 2. 查询角色已分配的菜单ID列表
     */
    @GetMapping("/permission-ids/{roleId}")
    public Result<List<Long>> getPermissionIdsByRoleId(@PathVariable Long roleId) {
        log.info("查询角色{}已分配的菜单ID", roleId);
        return rolePermissionService.getPermissionIdsByRoleId(roleId);
    }

    /**
     * 3. 查询角色可访问的完整菜单列表（带名称/路径等）
     */
    @GetMapping("/permissions/{roleId}")
    public Result<List<SysPermission>> getPermissionsByRoleId(@PathVariable Long roleId) {
        log.info("查询角色{}可访问的菜单列表", roleId);
        return rolePermissionService.getPermissionsByRoleId(roleId);
    }

    /**
     * 4. 查询所有角色（用于权限分配页面下拉选择）
     */
    @GetMapping("/roles")
    public Result<List<Role>> selectAllRoles() {
        return roleService.selectAllRoles();
    }
}