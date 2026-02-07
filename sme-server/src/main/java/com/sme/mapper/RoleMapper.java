package com.sme.mapper;

import com.sme.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper
 */
@Mapper
public interface RoleMapper {

    /**
     * 根据用户ID查询角色列表
     * @param userId
     * @return
     */
    List<Role> findRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询角色列表
     * @param id
     * @return
     */
    Role findById(@Param("id") Long id);
}
