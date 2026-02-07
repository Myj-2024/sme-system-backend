package com.sme.mapper;

import com.sme.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限Mapper
 */
@Mapper
public interface PermissionMapper {

    /**
     * 根据角色ID查询权限列表
     * @param roleId
     * @return
     */
    List<Permission> findPermissionsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询权限列表
     * @param userId
     * @return
     */
    List<Permission> findPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 根据权限编码查询权限信息
     * @param code
     * @return
     */
    Permission findByCode(@Param("code") String code);


}
