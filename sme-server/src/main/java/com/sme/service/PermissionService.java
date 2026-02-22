package com.sme.service;

import com.sme.dto.PermissionPageQueryDTO;
import com.sme.entity.SysPermission;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.vo.PermissionVO;

import java.util.List;

public interface PermissionService {

    // 分页查询
    PageResult getPermissions(PermissionPageQueryDTO pageDTO);

    // 根据ID查询
    SysPermission getPermissionById(Long id);

    // 新增权限
    Result<SysPermission> addPermission(SysPermission permission);

    // 更新权限
    Result<SysPermission> updatePermission(SysPermission permission);

    // 删除权限
    Result<SysPermission> deletePermission(Long id);

    // 根据角色ID查询菜单（树形结构）
    List<PermissionVO> getMenuByRoleId(Long roleId);

    // 查询所有权限
    List<SysPermission> selectAllPermissions();
}