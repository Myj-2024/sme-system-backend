package com.sme.mapper;

import com.github.pagehelper.Page;
import com.sme.dto.PermissionPageQueryDTO;
import com.sme.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionMapper {

    // 分页查询
    Page<SysPermission> getPermissions(PermissionPageQueryDTO pageDTO);

    // 根据ID查询
    SysPermission getPermissionById(Long id);

    // 新增权限
    void addPermission(SysPermission permission);

    // 更新权限
    int updatePermission(SysPermission permission);

    // 删除权限（逻辑删除）
    int deletePermission(Long id);

    // 路径唯一性校验
    int checkPathUnique(@Param("path") String path, @Param("id") Long id);

    // 查询所有未删除的权限
    List<SysPermission> selectAllPermissions();

    // 根据角色ID查询可访问的菜单
    List<SysPermission> selectMenuByRoleId(@Param("roleId") Long roleId);

    // 新增：根据路由名称查询（唯一性校验）
    int checkRouteNameUnique(@Param("routeName") String routeName, @Param("id") Long id);
}