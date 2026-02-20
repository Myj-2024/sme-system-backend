package com.sme.mapper;

import com.github.pagehelper.Page;
import com.sme.dto.RolePageQueryDTO;
import com.sme.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {


    /**
     * 分页查询角色（支持多条件筛选）【原方法保留，调整命名更语义化】
     */
    Page<Role> pageQuery(RolePageQueryDTO rolePageQueryDTO);

    /**
     * 查询所有未删除的角色（供管理员使用）【原方法保留，调整命名更语义化】
     */
    List<Role> selectAllRoles();

    /**
     * 新增角色
     */
    int addRole(Role role);

    /**
     * 检查角色编码是否已存在【原方法保留，调整命名更语义化】
     */
    int checkRoleCodeUnique(@Param("roleCode") String roleCode,
                            @Param("id") Long id);

    /**
     * 修改角色
     */
    void updateRole(Role role);

    /**
     * 根据ID查询角色【原方法保留，调整命名更语义化】
     */
    Role getRoleById(Long id);

    /**
     * 删除角色【原方法保留，调整命名更语义化】
     */
    void deleteRole(Long id);
}
