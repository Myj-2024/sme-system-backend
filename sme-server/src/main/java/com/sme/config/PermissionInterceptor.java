package com.sme.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sme.result.Result;
import com.sme.service.PermissionService;
import com.sme.service.UserService;
import com.sme.utils.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 权限拦截器
 * 用来实现SpringMVC拦截器，用于在请求处理之前进行权限验证
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {


    @Autowired
    private PermissionService permissionService;

    /**
     * JSON处理器
     */
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //如果不是映射到方法就直接通过（比如 静态资源）
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //首先检查方法上是否有权限注解
        RequiresPermission requiresPermission = method.getAnnotation(RequiresPermission.class);
        if (requiresPermission == null) {
            //如果该方法上没有这个权限注解，再检查类级别是否有权限注解
            requiresPermission = handlerMethod.getBeanType().getAnnotation(RequiresPermission.class);
        }

        //如果没有权限注解，则直接通过
        if (requiresPermission == null) {
            return true;
        }

        //从用户上下文当中获取用户ID
        Long userId = UserContext.getUserId();
        if (userId == null) {
            sendError(response, "用户未登录");
            return false;
    }
        //获取注解中定义的所需权限编码数组
        String[] permissions = requiresPermission.value();
        
        //检查用户是否拥有所需权限
        for (String permission : permissions) {
            if (permissionService.hasPermission(userId, permission)){
                return true;
            }
        }

        //如果用户没有所需权限，则返回错误信息
        sendError(response, "用户没有权限");

        return false;
    }

    /**
     * 响应错误信息
     * @param response
     * @param message
     * @throws Exception
     */
    private void sendError(HttpServletResponse response, String message) throws Exception {
        //设置状态码为403
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        //设置响应内容类型为JSON
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        //构建统一的错误信息
        Result<Object> result = Result.forbidden(message);
        //将结果对象转换为JSON字符串并写入响应
        writer.write(objectMapper.writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}
