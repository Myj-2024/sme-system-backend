package com.sme.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sme.dto.RoleDTO;
import com.sme.dto.RolePageQueryDTO;
import com.sme.entity.Role;
import com.sme.mapper.RoleMapper;
import com.sme.result.PageResult;
import com.sme.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;


    @Override
    public PageResult findRolesByUserId(RolePageQueryDTO rolePageQueryDTO) {

        PageHelper.startPage(rolePageQueryDTO.getPageNum(), rolePageQueryDTO.getPageSize());
        Page<Role> page = roleMapper.pageQuery(rolePageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Role findById(Long id) {
        return roleMapper.findById(id);
    }

    @Override
    public List<Role> findAll() {
        return roleMapper.findAll();
    }

    @Override
    @Transactional // 建议开启事务，涉及两张表的改动
    public void createRole(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);

        // 1. 补全缺失的基础信息（解决日志中 status 为 null 的问题）
        if (role.getStatus() == null) {
            role.setStatus(1); // 默认启用
        }
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        role.setDelFlag((byte) 0);

        // 2. 插入角色表
        roleMapper.insert(role);

        // 3. 关联权限（此时 role.getId() 已经被 MyBatis 回填）
        if (roleDTO.getPermissionIds() != null && !roleDTO.getPermissionIds().isEmpty()) {
            this.assignPermissions(role.getId(), roleDTO.getPermissionIds());
        }
    }

    @Override
    @Transactional
    public void updateRole(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);

        // 1. 更新时强制刷新更新时间
        role.setUpdateTime(new Date());

        // 2. 更新基础信息
        roleMapper.update(role);

        // 3. 更新权限关联（先删后增）
        if (roleDTO.getPermissionIds() != null) {
            this.assignPermissions(role.getId(), roleDTO.getPermissionIds());
        }
    }

    @Override
    public void deleteRole(Long id) {
        roleMapper.delete(id);
        roleMapper.deleteRolePermission(id);
    }

    @Override
    public List<Long> getRolePermissionIds(Long roleId) {
        return roleMapper.findPermissionIdsByRoleId(roleId);
    }

    @Override
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        // 先删除原有的权限映射
        roleMapper.deleteRolePermission(roleId);

        // 批量插入（如果 Mapper 支持批量插入更好，这里是你的循环插入逻辑）
        if (permissionIds != null && !permissionIds.isEmpty()) {
            permissionIds.forEach(pid -> roleMapper.insertRolePermission(roleId, pid));
        }
    }
}
