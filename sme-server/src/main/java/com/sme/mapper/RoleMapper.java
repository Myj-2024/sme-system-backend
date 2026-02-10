package com.sme.mapper;

import com.sme.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    List<Role> findRolesByUserId(@Param("userId") Long userId);

    Role findById(@Param("id") Long id);

    void insert(Role role);

    void update(Role role);

    void delete(@Param("id") Long id);

    void insertRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    void deleteRolePermission(@Param("roleId") Long roleId);

    List<Long> findPermissionIdsByRoleId(@Param("roleId") Long roleId);

    List<Role> findAll();
}
