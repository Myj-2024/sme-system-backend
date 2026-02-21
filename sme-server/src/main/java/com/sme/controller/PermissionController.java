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

    /**
     * 根据角色ID查询该角色可访问的菜单列表（树形结构）
     */
    @GetMapping("/getRoleMenu/{roleId}")
    @Operation(summary = "根据角色ID查菜单", description = "返回指定角色可访问的菜单列表（含路由/图标信息）")
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
            List<PermissionVO> menuList = sysPermissionService.getMenuByRoleId(roleId);
            return Result.success(menuList);
        } catch (Exception e) {
            log.error("根据角色ID={}查询菜单失败", roleId, e);
            return Result.error(MessageConstant.PERMISSION_QUERY_ERROR);
        }
    }
}