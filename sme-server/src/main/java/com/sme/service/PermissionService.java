package com.sme.service;

import com.sme.dto.PermissionPageQueryDTO;
import com.sme.entity.SysPermission;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.vo.PermissionVO;

import java.util.List;

public interface PermissionService {
    // 修改：返回PageResult分页对象（对齐用户管理）
    PageResult getPermissions(PermissionPageQueryDTO pageDTO);

    SysPermission getPermissionById(Long id);

    Result<SysPermission> addPermission(SysPermission permission);

    Result<SysPermission> updatePermission(SysPermission permission);

    Result<SysPermission> deletePermission(Long id);

    List<PermissionVO> getMenuByRoleId(Long roleId);
}