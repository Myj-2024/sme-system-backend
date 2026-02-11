package com.sme.mapper;

import com.github.pagehelper.Page;
import com.sme.dto.RolePageQueryDTO;
import com.sme.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {


    Role findById(@Param("id") Long id);

    void insert(Role role);

    void update(Role role);

    void delete(@Param("id") Long id);

    void insertRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    void deleteRolePermission(@Param("roleId") Long roleId);

    List<Long> findPermissionIdsByRoleId(@Param("roleId") Long roleId);

    List<Role> findAll();

    /**
     * 分页查询角色（支持多条件筛选）【原方法保留，调整命名更语义化】
     */
    Page<Role> pageQuery(RolePageQueryDTO rolePageQueryDTO);

    /**
     * 新增：根据用户ID查询关联的所有角色（不分页）【解决核心报错的关键方法】
     */
    List<Role> findRolesByUserId(@Param("userId") Long userId);
}
