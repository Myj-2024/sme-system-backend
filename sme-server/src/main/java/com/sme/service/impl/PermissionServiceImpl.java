package com.sme.service.impl;

import com.sme.entity.SysPermission;
import com.sme.entity.SysPermission;
import com.sme.mapper.PermissionMapper;
import com.sme.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<SysPermission> findPermissionsByRoleId(Long roleId) {
        return permissionMapper.findPermissionsByRoleId(roleId);
    }

    @Override
    public List<SysPermission> findPermissionsByUserId(Long userId) {
        return permissionMapper.findPermissionsByUserId(userId);
    }

    @Override
    public SysPermission findByCode(String code) {
        return permissionMapper.findByCode(code);
    }

    @Override
    public Boolean hasPermission(Long userId, String permissionCode) {
        List<SysPermission> permissions = permissionMapper.findPermissionsByUserId(userId);
        return permissions.stream().anyMatch(permission -> permission.getCode().equals(permissionCode));
    }

    @Override
    public void createPermission(SysPermission permission) {
        permissionMapper.insert(permission);
    }

    @Override
    public void updatePermission(SysPermission permission) {
        permissionMapper.update(permission);
    }

    @Override
    public void deletePermission(Long id) {
        permissionMapper.delete(id);
    }

    @Override
    public List<SysPermission> findAll() {
        return permissionMapper.findAll();
    }

    @Override
    public List<SysPermission> findUserMenuTree(Long userId) {
        // 1. 获取该用户有权访问的所有菜单（扁平列表）
        List<SysPermission> allMenus = permissionMapper.selectMenuByUserId(userId);

        // 2. 构建树形结构 (根节点 parentId 一般为 0)
        return buildTree(allMenus, 0L);
    }

    @Override
    public List<String> findUserPermissionCodes(Long userId) {
        return permissionMapper.selectPermissionCodesByUserId(userId);
    }

    /**
     * 递归构建菜单树
     */
    private List<SysPermission> buildTree(List<SysPermission> menus, Long parentId) {
        List<SysPermission> tree = new ArrayList<>();
        for (SysPermission menu : menus) {
            if (parentId.equals(menu.getParentId())) {
                // 递归查找子节点
                List<SysPermission> children = buildTree(menus, menu.getId());
                menu.setChildren(children);
                tree.add(menu);
            }
        }
        return tree;
    }
}

