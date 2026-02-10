package com.sme.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    /**
     * 根据用户ID查询已拥有的角色ID
     */
    @Select("SELECT role_id FROM user_role WHERE user_id = #{userId}")
    List<Long> findRoleIdsByUserId(Long userId);

    /**
     * 根据用户ID删除所有关联（用于更新前的清空）
     */
    @Delete("DELETE FROM user_role WHERE user_id = #{userId}")
    void deleteByUserId(Long userId);

    /**
     * 批量插入关联关系
     */
    void batchInsert(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
}
