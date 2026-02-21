package com.sme.mapper;

import com.github.pagehelper.Page;
import com.sme.dto.PermissionPageQueryDTO;
import com.sme.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 权限Mapper
 */
@Mapper
public interface PermissionMapper {

    // 修改：返回Page类型，适配PageHelper分页
    Page<SysPermission> getPermissions(PermissionPageQueryDTO pageDTO);

    SysPermission getPermissionById(Long id);

    void addPermission(SysPermission permission);

    int updatePermission(SysPermission permission);

    int deletePermission(Long id);


    int checkPathUnique(@Param("path") String path, @Param("id") Long id);

    /**
     * 查询所有未删除的菜单权限（供管理员使用）
     */
    List<SysPermission> selectAllPermissions();

    /**
     * 根据角色ID查询可访问的菜单（统一返回List<SysPermission>）
     */
    List<SysPermission> selectMenuByRoleId(@Param("roleId") Long roleId);
}