package com.sme.controller;

import com.sme.dto.RolePageQueryDTO;
import com.sme.entity.Role;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/role")
@Tag(name = "角色管理接口", description = "角色管理接口")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 角色分页查询（完善实现）
     */
    @GetMapping("/page")
    @Operation(summary = "角色分页查询", description = "角色分页查询")
    public Result<PageResult> page(RolePageQueryDTO rolePageQueryDTO) {
        log.info("角色分页查询参数：{}", rolePageQueryDTO);
        PageResult pageResult = roleService.page(rolePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增：查询所有角色（复用）
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有角色", description = "查询所有角色")
    public Result<List<Role>> selectAllRoles() {
        return roleService.selectAllRoles();
    }

    /**
     * 新增角色
     */
    @PostMapping("/add")
    @Operation(summary = "新增角色")
    public Result<Void> add(@RequestBody Role role) {
        log.info("新增角色：{}", role);
        return roleService.add(role);
    }

    /**
     * 修改角色
     */
    @PutMapping("/update")
    @Operation(summary = "修改角色")
    public Result update(@RequestBody Role role) {
        if (role.getId() == null) {
            return Result.error("角色ID不能为空");
        }

        // 直接返回 service 的结果
        return roleService.update(role);
    }


    /**
     * 根据id查询角色
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询角色")
    public Result<Role> getById(@PathVariable Long id) {
        return roleService.getById(id);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除角色")
    public Result<Void> delete(@PathVariable Long id) {
        return roleService.delete(id);
    }
}