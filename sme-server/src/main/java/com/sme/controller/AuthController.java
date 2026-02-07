package com.sme.controller;

import com.sme.constant.MessageConstant;
import com.sme.dto.UserLoginDTO;
import com.sme.result.Result;
import com.sme.service.UserService;
import com.sme.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/auth")
@Tag(name = "用户登录", description = "用户登录接口")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param userLoginDTO 用户登录信息
     * @return 用户登录结果
     */
    @PostMapping("/login")
    public Result<UserLoginVO> Login(@RequestBody UserLoginDTO userLoginDTO){
        try{
            UserLoginVO userLoginVO = userService.login(userLoginDTO);
            return Result.success(userLoginVO);
        }catch (Exception e){
            return Result.error(MessageConstant.USER_LOGIN_ERROR);
        }
    }
}
