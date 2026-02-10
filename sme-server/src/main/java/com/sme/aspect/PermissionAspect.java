package com.sme.aspect;

import com.sme.annotation.RequirePermission;
import com.sme.entity.SysPermission;
import com.sme.exception.BaseException;
import com.sme.result.Result;
import com.sme.result.ResultCode;
import com.sme.service.PermissionService;
import com.sme.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class PermissionAspect {

    @Autowired
    private PermissionService permissionService;

    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) throws Throwable {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            // 这里建议抛出自定义异常，由全局异常处理器捕获返回 401
            throw new BaseException("登录已失效");
        }

        // 1. 获取接口要求的权限列表和逻辑关系
        String[] requiredPermissions = requirePermission.value();
        RequirePermission.Logical logical = requirePermission.logical();

        // 2. 获取当前用户拥有的所有权限编码
        // 建议：在 permissionService 中对该方法加本地缓存或 Redis 缓存
        List<SysPermission> userPermissions = permissionService.findPermissionsByUserId(userId);
        Set<String> userPermissionCodes = userPermissions.stream()
                .map(SysPermission::getCode)
                .collect(Collectors.toSet());

        // 3. 权限判定
        boolean hasAuth = false;
        if (logical == RequirePermission.Logical.OR) {
            hasAuth = Arrays.stream(requiredPermissions).anyMatch(userPermissionCodes::contains);
        } else {
            hasAuth = Arrays.stream(requiredPermissions).allMatch(userPermissionCodes::contains);
        }

        if (!hasAuth) {
            log.warn("用户 {} 尝试访问无权限接口，所需权限：{}", userId, Arrays.toString(requiredPermissions));
            // 返回统一的 403 结果
            return Result.error(ResultCode.NO_PERMISSION);
        }

        return joinPoint.proceed();
    }
}