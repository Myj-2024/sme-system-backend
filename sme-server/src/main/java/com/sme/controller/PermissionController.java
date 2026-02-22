package com.sme.controller;

import com.sme.constant.MessageConstant;
import com.sme.dto.PermissionPageQueryDTO;
import com.sme.entity.SysPermission;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.result.ResultCode;
import com.sme.service.PermissionService;
import com.sme.vo.PermissionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/permission")
@Tag(name = "菜单管理", description = "菜单权限及路由配置管理")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 分页查询权限列表
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询权限", description = "支持按名称/编码模糊查询")
    public Result<PageResult> getPermissions(@RequestBody PermissionPageQueryDTO pageDTO) {
        log.info("分页查询权限列表：{}", pageDTO);
        PageResult pageResult = permissionService.getPermissions(pageDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据ID查询权限详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询权限详情", description = "返回权限完整信息（包含路由配置）")
    public Result<SysPermission> getPermissionById(
            @Parameter(description = "权限ID", required = true)
            @PathVariable Long id) {
        log.info("查询权限详情：ID={}", id);
        SysPermission permission = permissionService.getPermissionById(id);
        if (permission == null) {
            return Result.error(ResultCode.PARAM_ERROR);
        }
        return Result.success(permission);
    }

    /**
     * 新增权限
     */
    @PostMapping("/add")
    @Operation(summary = "新增权限", description = "新增菜单/按钮权限，自动处理路由配置")
    public Result<SysPermission> addPermission(@RequestBody SysPermission permission) {
        log.info("新增权限：{}", permission);
        return permissionService.addPermission(permission);
    }

    /**
     * 更新权限
     */
    @PostMapping("/update")
    @Operation(summary = "更新权限", description = "更新权限信息及路由配置")
    public Result<SysPermission> updatePermission(@RequestBody SysPermission permission) {
        log.info("更新权限：{}", permission);
        return permissionService.updatePermission(permission);
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除权限", description = "逻辑删除权限，有子节点时禁止删除")
    public Result<?> deletePermission(
            @Parameter(description = "权限ID", required = true)
            @PathVariable Long id) {
        log.info("删除权限：ID={}", id);
        return permissionService.deletePermission(id);
    }

    /**
     * 根据角色ID查询该角色可访问的菜单列表（树形结构）
     */
    @GetMapping("/getRoleMenu/{roleId}")
    @Operation(summary = "根据角色ID查菜单", description = "返回指定角色可访问的菜单列表（含路由配置）")
    public Result<List<PermissionVO>> getRoleMenu(
            @Parameter(description = "角色ID", required = true)
            @PathVariable Long roleId) {
        try {
            // 1. 校验角色ID
            if (roleId == null || roleId <= 0) {
                log.warn("角色ID非法：{}", roleId);
                return Result.error(ResultCode.PARAM_ERROR);
            }

            // 2. 调用Service获取菜单
            List<PermissionVO> menuList = permissionService.getMenuByRoleId(roleId);
            return Result.success(menuList);
        } catch (Exception e) {
            log.error("根据角色ID={}查询菜单失败", roleId, e);
            return Result.error(MessageConstant.PERMISSION_QUERY_ERROR);
        }
    }

    /**
     * 查询所有权限列表（用于下拉选择）
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有权限", description = "返回所有未删除的权限列表")
    public Result<List<SysPermission>> listAll() {
        List<SysPermission> list = permissionService.selectAllPermissions();
        return Result.success(list);
    }
}