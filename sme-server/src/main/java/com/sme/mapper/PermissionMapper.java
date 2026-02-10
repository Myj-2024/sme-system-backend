package com.sme.mapper;

import com.sme.entity.SysPermission;
import com.sme.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限Mapper
 */
@Mapper
public interface PermissionMapper {

    // 根据角色ID查询权限列表
    List<SysPermission> findPermissionsByRoleId(@Param("roleId") Long roleId);

    // 根据用户ID查询权限列表
    List<SysPermission> findPermissionsByUserId(@Param("userId") Long userId);

    // 根据权限编码查询权限信息
    SysPermission findByCode(@Param("code") String code);

    // 插入权限
    void insert(SysPermission permission);

    // 更新权限
    void update(SysPermission permission);

    // 删除权限
    void delete(@Param("id") Long id);

    /**
     * 查询所有权限列表
     */
    List<SysPermission> findAll();

    /**
     * 根据用户ID查询菜单类型的权限 (用于动态菜单)
     */
    List<SysPermission> selectMenuByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询所有权限编码 (用于按钮权限控制)
     */
    List<String> selectPermissionCodesByUserId(@Param("userId") Long userId);
}

