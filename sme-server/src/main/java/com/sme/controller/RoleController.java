package com.sme.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sme.dto.RoleDTO;
import com.sme.entity.Role;
import com.sme.result.Result;
import com.sme.service.RoleService;
import com.sme.annotation.RequirePermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/role")
@RequirePermission("sys:role:manage") // 必须有角色管理权限才能调用此控制器
@Tag(name = "角色管理接口")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/page")
    public Result<Map<String, Object>> getRolePage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Role> list = roleService.findAll();
        PageInfo<Role> pageInfo = new PageInfo<>(list);
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        return Result.success(result);
    }

    @GetMapping
    public Result<List<Role>> getAllRoles() {
        return Result.success(roleService.findAll());
    }

    @PostMapping
    public Result<Void> createRole(@RequestBody RoleDTO roleDTO) {
        roleService.createRole(roleDTO);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        roleDTO.setId(id);
        roleService.updateRole(roleDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success();
    }

    @PostMapping("/{id}/permissions")
    public Result<Void> assignPermissions(@PathVariable Long id, @RequestBody List<Long> permissionIds) {
        roleService.assignPermissions(id, permissionIds);
        return Result.success();
    }

    /**
     * 获取角色详情
     * 对应前端 roleApi.getRoleById
     */
    @GetMapping("/{id}")
    @RequirePermission("sys:role:manage")
    public Result<Role> getRoleById(@PathVariable Long id) {
        Role role = roleService.findById(id);
        return Result.success(role);
    }

    /**
     * 获取角色拥有的权限ID集合
     * 修复报错：对应前端 roleApi.getRolePermissions (GET方式)
     */
    @GetMapping("/{id}/permissions")
    @RequirePermission("sys:role:manage")
    public Result<List<Long>> getRolePermissions(@PathVariable Long id) {
        // 调用 Service 中定义的 getRolePermissionIds
        List<Long> permissionIds = roleService.getRolePermissionIds(id);
        return Result.success(permissionIds);
    }

}