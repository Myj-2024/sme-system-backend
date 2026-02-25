package com.sme.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sme.dto.RolePageQueryDTO;
import com.sme.entity.Role;
import com.sme.mapper.RoleMapper;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 分页查询
     * @param dto
     * @return
     */
    @Override
    public PageResult page(RolePageQueryDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        Page<Role> page = roleMapper.pageQuery(dto);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 查询所有角色
     * @return
     */
    @Override
    public Result<List<Role>> selectAllRoles() {
        List<Role> roles = roleMapper.selectAllRoles();
        return Result.success(roles);
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    @Override
    public Result<Void> add(Role role) {
        if (roleMapper.checkRoleCodeUnique(role.getRoleCode(), null) > 0) {
            return Result.error("角色编码【" + role.getRoleCode() + "】已存在，请勿重复添加！");
        }
        roleMapper.addRole(role);
        return Result.success();
    }


    /**
     * 修改角色
     * @param role
     * @return
     */
    @Override
    public Result<Void> update(Role role) {

        if (role == null) {
            return Result.error("角色数据不能为空！");
        }

        if (role.getId() == null) {
            return Result.error("角色ID不能为空，无法更新！");
        }

        if (role.getRoleCode() == null || role.getRoleCode().trim().isEmpty()) {
            return Result.error("角色编码不能为空！");
        }

        // 关键点：传入当前ID
        if (roleMapper.checkRoleCodeUnique(role.getRoleCode(), role.getId()) > 0) {
            return Result.error("角色编码【" + role.getRoleCode() + "】已存在，请勿重复添加！");
        }

        roleMapper.updateRole(role);

        return Result.success("更新成功");
    }


    /**
     * 根据ID查询角色
     * @param id
     * @return
     */
    @Override
    public Result<Role> getById(Long id) {
        Role role = roleMapper.getRoleById(id);
        return Result.success(role);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @Override
    public Result<Void> delete(Long id) {
        roleMapper.deleteRole(id);
        return Result.success();
    }
}