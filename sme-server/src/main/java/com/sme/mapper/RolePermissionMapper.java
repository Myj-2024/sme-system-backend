package com.sme.mapper;


import com.sme.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RolePermissionMapper {

    /**
     * 批量保存角色-菜单关联关系
     */
    void batchSave(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);

    /**
     * 物理删除角色已分配的所有权限（适配现有表无del_flag）
     */
    void deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询角色已分配的菜单ID列表
     */
    List<Long> selectPermissionIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询角色可访问的菜单列表
     */
    List<SysPermission> selectPermissionsByRoleId(@Param("roleId") Long roleId);
}
