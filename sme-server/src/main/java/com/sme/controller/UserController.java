package com.sme.controller;


import com.sme.constant.MessageConstant;
import com.sme.dto.UserPageQueryDTO;
import com.sme.entity.Role;
import com.sme.entity.User;
import com.sme.exception.BaseException;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.result.ResultCode;
import com.sme.service.UserService;
import com.sme.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/user")
@Tag(name = "用户管理", description = "用户管理接口")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 获取所有用户
     * @return
     */
    @GetMapping
    @Operation(summary = "获取所有用户", description = "返回所有用户列表")
    public Result<List<User>> getAllUsers() {
        List<User> userList = userService.findAll();
        if (userList == null) {
            return Result.error(MessageConstant.USER_NOT_FOUND);
        }
        return Result.success(userList);
    }

    /**
     * 根据ID查询用户
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取用户信息", description = "返回用户信息")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
    }

    /**
     * 分页查询用户列表
     *
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询", description = "分页查询用户列表")
    public Result<PageResult> getUserPage(UserPageQueryDTO userPageQueryDTO) {
            log.info("分页查询用户列表：{}", userPageQueryDTO);
            PageResult page =userService.pageQuery(userPageQueryDTO);
            PageResult pageResult = new PageResult(page.getTotal(), page.getRecords());
            return Result.success(pageResult);
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    @PostMapping("")
    @Operation(summary = "新增用户", description = "返回用户信息")
    public Result insertUser(@RequestBody User user) {
        boolean result = userService.insert(user);
        if (result) {
            return Result.success();
        }
        return Result.error(MessageConstant.USER_INSERT_ERROR);
    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改用户", description = "返回用户信息")
    public Result updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        boolean result = userService.update(user);
        if (result) {
            return Result.success();
        }
        return Result.error(MessageConstant.USER_UPDATE_ERROR);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "返回删除结果")
    public Result deleteUser(@PathVariable Long id) {
        boolean result = userService.deleteById(id);
        if (result) {
            return Result.success();
        }
        return Result.error(MessageConstant.USER_DELETE_ERROR);
    }

    /**
     * 修改用户状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @Operation(summary = "修改用户状态", description = "返回修改结果")
    public Result updateUserStatus(@PathVariable Integer status, @RequestParam Long id) {
        log.info("修改用户状态：{}", status);
        try {
            if (id == null) return Result.error(MessageConstant.USER_NOT_EXIST);
            if (status == null || (status != 0 && status != 1)) return Result.error(MessageConstant.USER_STATUS_ERROR);
            userService.updateUserStatus(status, id);
            return Result.success();
        } catch (BaseException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error(MessageConstant.USER_UPDATE_ERROR);
        }
    }



    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息", description = "返回用户信息、菜单树以及按钮权限码")
    public Result<Map<String, Object>> getCurrentUserInfo() {
        // 1. 获取当前登录用户ID
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        // 2. 获取基本用户信息
        User user = userService.findById(userId);
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        // 安全起见：抹除密码哈希值，不传给前端
        user.setPassword(null);

        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        return Result.success(data);

    }

    /**
     * 修改用户角色
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/{id}/role")
    public Result updateRole(@PathVariable Long id, @RequestBody User user) {
        // 1. 基础参数校验
        Assert.notNull(id, "用户ID不能为空");
        Assert.notNull(user.getRoleId(), "角色ID不能为空");
        Assert.isTrue(user.getRoleId() != 0, "角色ID不能为0");

        // 2. 构建仅包含需要更新字段的用户对象，防止其他字段被篡改
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setRoleId(user.getRoleId()); // 只更新角色ID

        // 3. 执行更新操作
        boolean result = userService.updateRole(updateUser);
        if (result) {
            return Result.success("角色分配成功");
        }
        return Result.error(MessageConstant.USER_UPDATE_ERROR);
    }

    /**
     * 重置密码
     * @param id
     * @return
     */
    @PostMapping("/resetPwd/{id}")
    public Result resetPassword(@PathVariable Long id) {
        log.info("重置密码：{}", id);
        userService.resetPassword(id);
        return Result.success(MessageConstant.USER_PASSWORD_RESET_SUCCESS);
    }


}