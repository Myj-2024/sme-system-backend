package com.sme.service.impl;

import com.sme.entity.SysPermission;
import com.sme.entity.SysPermission;
import com.sme.mapper.PermissionMapper;
import com.sme.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {


    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 根据角色ID查询权限列表
     * @param roleId
     * @return
     */
    @Override
    public List<SysPermission> findPermissionsByRoleId(Long roleId) {
        return permissionMapper.findPermissionsByRoleId(roleId);
    }

    /**
     * 根据用户ID查询权限列表
     * @param userId
     * @return
     */
    @Override
    public List<SysPermission> findPermissionsByUserId(Long userId) {
        return permissionMapper.findPermissionsByUserId(userId);
    }

    /**
     * 根据权限编码查询权限信息
     * @param code
     * @return
     */
    @Override
    public SysPermission findByCode(String code) {
        return permissionMapper.findByCode( code);
    }

    /**
     * 判断用户是否拥有权限
     * @param userId
     * @param permissionCode
     * @return
     */
    @Override
    public Boolean hasPermission(Long userId, String permissionCode) {
        List<SysPermission> permissions = permissionMapper.findPermissionsByUserId(userId);
        return permissions.stream()
                .anyMatch(permission -> permission.getCode().equals(permissionCode));
    }
}
