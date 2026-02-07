package com.sme.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sme.constant.MessageConstant;
import com.sme.entity.User;
import com.sme.exception.BaseException;
import com.sme.result.Result;
import com.sme.result.ResultCode;
import com.sme.service.UserService;
import com.sme.utils.PageUtils;
import com.sme.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
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
    @GetMapping("/{id}")
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

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<User> getCurrentUserInfo(){
         Long userId =UserContext.getUserId();
         if (userId != null){
             User user = userService.findById(userId);
             if (user != null){
                 return Result.success(user);
             }
         }
         return Result.error(ResultCode.USER_NOT_EXIST);
    }

    /**
     * 添加用户
     */
    @PostMapping("")
    public Result insertUser(@RequestBody User user){
        boolean result = userService.insert(user);
        if (result){
            return Result.success();
        }
        return Result.error(MessageConstant.USER_INSERT_ERROR);
    }

    /**
     * 修改用户
     */
    @PutMapping("/{id}")
    public Result updateUser(@PathVariable Long id,@RequestBody User user){
        user.setId(id);
        boolean result = userService.update(user);
        if (result){
            return Result.success();
        }
        return Result.error(MessageConstant.USER_UPDATE_ERROR);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Long id){
        boolean result = userService.deleteById(id);
        if (result){
            return Result.success();
        }
        return Result.error(MessageConstant.USER_DELETE_ERROR);
    }

    /**
     * 修改用户状态
     */
    @PostMapping("/{status}")
    public Result updateUserStatus(@PathVariable Integer status,@RequestParam Long id){
        log.info("修改用户状态：{}",status);
        try {
            if (id == null){
                return Result.error(MessageConstant.USER_NOT_EXIST);
            }

            if (status == null || (status != 0 && status != 1)){
                return Result.error(MessageConstant.USER_STATUS_ERROR);
            }

            userService.updateUserStatus(status,id);
            return Result.success();
        } catch (BaseException e) {
            log.warn("修改用户状态失败：{}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改用户状态异常", e);
            return Result.error(MessageConstant.USER_UPDATE_ERROR);
        }
    }
}
