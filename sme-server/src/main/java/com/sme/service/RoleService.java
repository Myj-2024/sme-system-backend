package com.sme.service;

import com.sme.dto.RoleDTO;
import com.sme.dto.RolePageQueryDTO;
import com.sme.entity.Role;
import com.sme.result.PageResult;
import com.sme.result.Result;

import java.util.List;

public interface RoleService {

    PageResult page(RolePageQueryDTO rolePageQueryDTO);

    Result<List<Role>> selectAllRoles();


    Result<Void> add(Role role);


    Result<Void> update(Role role);

    Result<Role> getById(Long id);

    Result<Void> delete(Long id);
}
