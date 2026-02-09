package com.sme.service;

import com.sme.entity.SysPermission;
import com.sme.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限服务
 */
public interface PermissionService {

    /**
     * 根据角色ID查询权限列表
     * @param roleId
     * @return
     */
    List<SysPermission> findPermissionsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询权限列表
     * @param userId
     * @return
     */
    List<SysPermission> findPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 根据权限编码查询权限信息
     * @param code
     * @return
     */
    SysPermission findByCode(@Param("code") String code);

    /**
     * 检查用户是否有某个权限
     */
    Boolean hasPermission(@Param("userId") Long userId, @Param("permissionCode") String permissionCode);
}
