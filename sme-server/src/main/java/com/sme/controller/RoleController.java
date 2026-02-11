package com.sme.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sme.dto.RoleDTO;
import com.sme.dto.RolePageQueryDTO;
import com.sme.entity.Role;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.RoleService;
import com.sme.annotation.RequirePermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/admin/role")
@RequirePermission("sys:role:manage") // 必须有角色管理权限才能调用此控制器
@Tag(name = "角色管理接口")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/page")
    public Result<PageResult> page(RolePageQueryDTO rolePageQueryDTO) {
        log.info("分页查询参数：{}", rolePageQueryDTO);
        PageResult page = roleService.findRolesByUserId(rolePageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(), page.getRecords());
        return Result.success(pageResult);
    }

    /**
     * 获取所有角色
     * @return
     */
    @GetMapping
    public Result<List<Role>> getAllRoles() {
        return Result.success(roleService.findAll());
    }

    /**
     * 创建角色
     * @param roleDTO
     * @return
     */
    @PostMapping
    public Result<Void> createRole(@RequestBody RoleDTO roleDTO) {
        roleService.createRole(roleDTO);
        return Result.success();
    }

    /**
     * 修改角色
     * @param id
     * @param roleDTO
     * @return
     */
    @PutMapping("/{id}")
    public Result<Void> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        roleDTO.setId(id);
        roleService.updateRole(roleDTO);
        return Result.success();
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success();
    }

    /**
     * 分配权限
     * @param id
     * @param permissionIds
     * @return
     */
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