package com.sme.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sme.constant.MessageConstant;
import com.sme.entity.User;
import com.sme.result.Result;
import com.sme.result.ResultCode;
import com.sme.service.UserService;
import com.sme.utils.PageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/user")
@Tag(name = "用户管理",description = "用户管理接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询用户列表
     * @return
     */
    @GetMapping
    public Result<List<User>> getAllUsers(){
        List<User> userList = userService.findAll();
        if(userList == null){
            return Result.error(MessageConstant.USER_NOT_FOUND);
        }
        return Result.success(userList);
    }

    /**
     * 根据ID查询用户
     * @param id
     * @return
     */
    @GetMapping("/id")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询",description = "分页查询用户列表")
    public Result<Map<String,Object>> getUserPage(
            @Parameter(name = "pageNum",description = "页码,默认值1",required = true) int pageNum,
            @Parameter(name = "pageSize",description = "页大小,默认值10",required = true) int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userService.findAll();
        PageInfo<User> pageInfo = new PageInfo<>(users);
        Map<String, Object> pageResult = PageUtils.toPageResult(pageInfo);
        return Result.success(pageResult);
    }
}
