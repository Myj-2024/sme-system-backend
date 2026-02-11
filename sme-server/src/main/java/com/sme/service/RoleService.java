package com.sme.service;

import com.sme.dto.RoleDTO;
import com.sme.dto.RolePageQueryDTO;
import com.sme.entity.Role;
import com.sme.result.PageResult;

import java.util.List;

public interface RoleService {

    PageResult findRolesByUserId(RolePageQueryDTO rolePageQueryDTO);

    Role findById(Long id);

    List<Role> findAll();

    void createRole(RoleDTO roleDTO);

    void updateRole(RoleDTO roleDTO);

    void deleteRole(Long id);

    List<Long> getRolePermissionIds(Long roleId);

    void assignPermissions(Long roleId, List<Long> permissionIds);
}
