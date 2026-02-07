package com.sme.controller;

import com.sme.constant.MessageConstant;
import com.sme.dto.UserLoginDTO;
import com.sme.result.Result;
import com.sme.service.UserService;
import com.sme.utils.JwtUtil;
import com.sme.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器（修复版）
 * 解决JwtUtil静态调用、参数校验缺失、日志缺失问题
 */
@Slf4j
@RestController
@RequestMapping("/admin/auth")
@Tag(name = "用户登录", description = "用户登录和Token刷新接口")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     * @param userLoginDTO 用户登录信息
     * @return 用户登录结果（包含JWT Token）
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "根据用户名和密码获取登录凭证")
    public Result<UserLoginVO> login(
            @Parameter(description = "登录信息", required = true)
            @RequestBody UserLoginDTO userLoginDTO){
        try{
            // 参数非空校验
            if (!StringUtils.hasText(userLoginDTO.getUsername()) ||
                    !StringUtils.hasText(userLoginDTO.getPassword())) {
                log.warn("登录参数为空：用户名={}, 密码={}", userLoginDTO.getUsername(),
                        userLoginDTO.getPassword() == null ? "null" : "***");
                return Result.error(MessageConstant.PARAMETER_ERROR);
            }

            log.info("用户登录请求：{}", userLoginDTO.getUsername());
            UserLoginVO userLoginVO = userService.login(userLoginDTO);
            log.info("用户登录成功：{}", userLoginDTO.getUsername());
            return Result.success(userLoginVO);
        }catch (Exception e){
            log.error("用户登录失败：{}", e.getMessage(), e);
            // 返回统一的错误提示，避免暴露敏感信息
            return Result.error(MessageConstant.USER_LOGIN_ERROR);
        }
    }

    /**
     * 刷新Token
     * @param token 请求头中的Authorization Token
     * @return 新的JWT Token
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新Token", description = "使用有效的旧Token获取新Token")
    public Result<String> refreshToken(
            @Parameter(description = "Bearer Token", required = true)
            @RequestHeader("Authorization") String token){
        try {
            // 1. 校验Token是否为空
            if (!StringUtils.hasText(token)) {
                log.warn("刷新Token失败：Token为空");
                return Result.error("Token不能为空");
            }

            // 2. 检查Token格式是否正确
            if (!token.startsWith("Bearer ")) {
                log.warn("刷新Token失败：Token格式错误，值={}", token);
                return Result.error("Token格式错误，请使用Bearer Token");
            }

            // 3. 提取纯Token并去除首尾空格
            String realToken = token.substring(7).trim();
            if (!StringUtils.hasText(realToken)) {
                log.warn("刷新Token失败：Token内容为空");
                return Result.error("Token内容不能为空");
            }

            // 4. 修复：使用实例化的jwtUtil验证Token（不再静态调用）
            if (jwtUtil.validateToken(realToken)) {
                // 5. 生成新Token
                String newToken = jwtUtil.refreshToken(realToken);
                log.info("Token刷新成功");
                return Result.success("Token刷新成功", newToken);
            } else {
                log.warn("刷新Token失败：Token无效或已过期，值={}", realToken);
                return Result.error("无效的Token（已过期或被篡改）");
            }
        }catch (Exception e){
            log.error("Token刷新异常", e);
            // 生产环境避免返回具体异常信息，防止泄露系统细节
            return Result.error("Token刷新失败，请重新登录");
        }
    }
}