package com.sme.controller;

import com.sme.entity.SysPermission;
import com.sme.result.Result;
import com.sme.service.PermissionService;
import com.sme.annotation.RequirePermission; // 确保引入注解
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/permission")
@RequirePermission("sys:manage") // 类级别：进入权限/菜单管理模块的基础权限
@Tag(name = "权限管理接口")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/role/{roleId}")
    @RequirePermission("sys:role:manage") // 角色授权时需要查看权限列表
    public List<SysPermission> getPermissionsByRoleId(@PathVariable Long roleId) {
        return permissionService.findPermissionsByRoleId(roleId);
    }

    @GetMapping("/user/{userId}")
    @RequirePermission("sys:user:list") // 查看用户详情时可能需要查看其权限
    public List<SysPermission> getPermissionsByUserId(@PathVariable Long userId) {
        return permissionService.findPermissionsByUserId(userId);
    }

    @GetMapping("/code/{code}")
    @RequirePermission("sys:menu:list")
    public SysPermission getPermissionByCode(@PathVariable String code) {
        return permissionService.findByCode(code);
    }

    @GetMapping("/check")
    @RequirePermission("sys:menu:list")
    public Boolean checkPermission(@RequestParam Long userId,
                                   @RequestParam String permissionCode) {
        return permissionService.hasPermission(userId, permissionCode);
    }

    @PostMapping
    @RequirePermission("sys:menu:add")
    public ResponseEntity<Void> createPermission(@RequestBody SysPermission permission) {
        permissionService.createPermission(permission);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @RequirePermission("sys:menu:edit")
    public ResponseEntity<Void> updatePermission(@PathVariable Long id, @RequestBody SysPermission permission) {
        permission.setId(id);
        permissionService.updatePermission(permission);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @RequirePermission("sys:menu:delete")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/tree")
    @RequirePermission("sys:manage")
    public Result<List<SysPermission>> getPermissionTree() {
        List<SysPermission> list = permissionService.findAll();
        return Result.success(list);
    }
}