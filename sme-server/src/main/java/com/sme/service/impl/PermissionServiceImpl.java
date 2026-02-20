package com.sme.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sme.dto.PermissionPageQueryDTO;
import com.sme.entity.SysPermission;
import com.sme.mapper.PermissionMapper;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    // 完全对齐用户管理的分页实现逻辑
    @Override
    public PageResult getPermissions(PermissionPageQueryDTO pageDTO) {
        // 1. 开启分页（PageHelper）
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());

        // 2. 执行分页查询
        Page<SysPermission> page = permissionMapper.getPermissions(pageDTO);

        // 3. 封装PageResult返回（对齐用户管理）
        return new PageResult(page.getTotal(), page.getResult());
    }

    // 以下方法完全保留原有逻辑，无修改
    @Override
    public SysPermission getPermissionById(Long id) {
        return permissionMapper.getPermissionById(id);
    }

    @Override
    public Result<SysPermission> addPermission(SysPermission permission) {

        if (permissionMapper.checkPathUnique(permission.getPath()) > 0) {
            // 返回明确的错误提示，方便前端展示
            return Result.error("权限路径【" + permission.getPath() + "】已存在，请勿重复添加！");
        }

        permissionMapper.addPermission(permission);
        return Result.success(permission);
    }

    @Override
    public Result<SysPermission> updatePermission(SysPermission permission) {
        if (permissionMapper.checkPathUnique(permission.getPath()) > 0) {
            // 返回明确的错误提示，方便前端展示
            return Result.error("权限路径【" + permission.getPath() + "】已存在，请勿重复添加！");
        }
        permissionMapper.updatePermission(permission);
        return Result.success(permission);
    }

    @Override
    public Result<SysPermission> deletePermission(Long id) {
        permissionMapper.deletePermission(id);
        return Result.success();
    }
}