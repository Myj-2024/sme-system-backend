package com.sme.service.impl;

import com.sme.dto.RolePermissionDTO;
import com.sme.entity.SysPermission;
import com.sme.mapper.PermissionMapper;
import com.sme.mapper.RolePermissionMapper;
import com.sme.result.Result;
import com.sme.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.List;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    // 管理员角色ID（适配你的数据：role_id=1是管理员）
    private static final Long ADMIN_ROLE_ID = 1L;

    /**
     * 分配权限：事务保证先删后加的原子性（物理删除）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> assignPermission(RolePermissionDTO dto) {
        // 参数校验
        if (dto.getRoleId() == null) {
            return Result.error("角色ID不能为空");
        }
        // 1. 先删除该角色已分配的所有权限（物理删除）
        rolePermissionMapper.deleteByRoleId(dto.getRoleId());
        // 2. 批量新增新权限（空列表则不新增）
        if (!CollectionUtils.isEmpty(dto.getPermissionIds())) {
            rolePermissionMapper.batchSave(dto.getRoleId(), dto.getPermissionIds());
        }
        return Result.success("权限分配成功");
    }

    /**
     * 查询角色已分配的菜单ID（管理员返回全部菜单ID）
     */
    @Override
    public Result<List<Long>> getPermissionIdsByRoleId(Long roleId) {
        if (roleId == null) {
            return Result.error("角色ID不能为空");
        }
        // 管理员角色：返回所有未删除的菜单ID
        if (ADMIN_ROLE_ID.equals(roleId)) {
            List<SysPermission> allPermissions = permissionMapper.selectAllPermissions();
            List<Long> allIds = allPermissions.stream().map(SysPermission::getId).toList();
            return Result.success(allIds);
        }
        // 普通角色：查询已分配的菜单ID
        List<Long> permissionIds = rolePermissionMapper.selectPermissionIdsByRoleId(roleId);
        return Result.success(permissionIds);
    }

    /**
     * 查询角色可访问的菜单列表（管理员返回全部）
     */
    @Override
    public Result<List<SysPermission>> getPermissionsByRoleId(Long roleId) {
        if (roleId == null) {
            return Result.error("角色ID不能为空");
        }
        // 管理员角色：直接返回所有未删除的菜单
        if (ADMIN_ROLE_ID.equals(roleId)) {
            List<SysPermission> allPermissions = permissionMapper.selectAllPermissions();
            return Result.success(allPermissions);
        }
        // 普通角色：返回关联表中绑定的菜单
        List<SysPermission> permissions = rolePermissionMapper.selectPermissionsByRoleId(roleId);
        return Result.success(permissions);
    }
}