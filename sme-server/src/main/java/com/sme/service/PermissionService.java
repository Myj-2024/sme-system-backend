package com.sme.service;

import com.sme.entity.SysPermission;
import com.sme.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限服务
 */
public interface PermissionService {

    List<SysPermission> findPermissionsByRoleId(Long roleId);

    List<SysPermission> findPermissionsByUserId(Long userId);

    SysPermission findByCode(String code);

    Boolean hasPermission(Long userId, String permissionCode);

    void createPermission(SysPermission permission);

    void updatePermission(SysPermission permission);

    void deletePermission(Long id);

    List<SysPermission> findAll();

    /**
     * 获取当前用户的动态菜单树
     */
    List<SysPermission> findUserMenuTree(Long userId);

    /**
     * 获取当前用户的权限标识集合
     */
    List<String> findUserPermissionCodes(Long userId);
}
