package com.sme.test;

import com.sme.config.RequiresPermission;
import com.sme.entity.User;
import com.sme.result.Result;
import com.sme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/test")
public class TestController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户列表
     */

    @GetMapping("/selectUserList")
    @RequiresPermission("sys:user:list")
    public Result<List<User>> selectUserList(){
        List<User> userList = userService.findAll();
        return Result.success(userList);
    }
}
