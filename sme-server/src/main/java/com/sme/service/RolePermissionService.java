package com.sme.service;

import com.sme.dto.RolePermissionDTO;
import com.sme.entity.SysPermission;
import com.sme.result.Result;
import java.util.List;

public interface RolePermissionService {

    /**
     * 给角色分配菜单权限（先删后加，物理删除）
     */
    Result<Void> assignPermission(RolePermissionDTO dto);

    /**
     * 查询角色已分配的菜单ID列表（管理员返回全部）
     */
    Result<List<Long>> getPermissionIdsByRoleId(Long roleId);

    /**
     * 查询角色可访问的菜单列表（管理员返回全部）
     */
    Result<List<SysPermission>> getPermissionsByRoleId(Long roleId);
}