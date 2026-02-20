package com.sme.controller;

import com.sme.dto.PermissionPageQueryDTO;
import com.sme.entity.SysPermission;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/permission")
@Tag(name = "菜单管理")
public class PermissionController {

    @Autowired
    private PermissionService sysPermissionService;

    // 分页查询接口（完全对齐用户管理的分页返回格式）
    @PostMapping("/page")
    public Result<PageResult> getPermissions(@RequestBody PermissionPageQueryDTO pageDTO) {
        PageResult pageResult = sysPermissionService.getPermissions(pageDTO);
        return Result.success(pageResult);
    }

    // 根据id查询接口（保持原有逻辑）
    @GetMapping("/{id}")
    public Result<SysPermission> getPermissionById(@PathVariable Long id) {
        SysPermission permission = sysPermissionService.getPermissionById(id);
        return Result.success(permission);
    }

    // 新增接口（保持原有逻辑）
    @PostMapping("/add")
    public Result<SysPermission> addPermission(@RequestBody SysPermission permission) {
        return sysPermissionService.addPermission(permission);
    }

    // 更新接口（保持原有逻辑）
    @PostMapping("/update")
    public Result<SysPermission> updatePermission(@RequestBody SysPermission permission) {
        return sysPermissionService.updatePermission(permission);
    }

    // 删除接口（保持原有逻辑）
    @DeleteMapping("/{id}")
    public Result<SysPermission> deletePermission(@PathVariable Long id) {
        return sysPermissionService.deletePermission(id);
    }
}