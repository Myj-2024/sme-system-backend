package com.sme.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sme.result.Result;
import com.sme.utils.JwtUtil;
import com.sme.utils.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

/**
 * JWT拦截器（修复版）
 * 解决ObjectMapper初始化错误、静态调用JwtUtil、URL匹配不严谨问题
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    // 修复：正确初始化ObjectMapper（移除null参数）
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 注入实例化的JwtUtil（不再用静态调用）
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 拦截器前置处理方法
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        String method = request.getMethod();
        log.info("拦截请求：{} {}", method, url);

        // 修复：精确匹配登录接口（避免模糊匹配导致的问题）
        if ("/admin/auth/login".equals(url) && "POST".equals(method)) {
            return true;
        }
        // 放行刷新Token接口
        if ("/admin/auth/refresh".equals(url) && "POST".equals(method)) {
            return true;
        }

        String token = request.getHeader("Authorization");

        // 验证Token存在且格式正确
        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
            log.warn("Token不存在或格式错误：{}", token);
            return handleUnauthorized(response, "未授权，请先登录");
        }

        // 提取纯Token
        String realToken = token.substring(7).trim();

        // 使用实例化的JwtUtil验证Token（核心修复）
        if (jwtUtil.validateToken(realToken)) {
            // 设置用户上下文
            Long userId = jwtUtil.getIdFromToken(realToken);
            String userName = jwtUtil.getUserNameFromToken(realToken);
            UserContext.setUserId(userId);
            UserContext.setUsername(userName);
            log.debug("Token验证通过，用户：{}", userName);
            return true;
        } else {
            log.warn("Token无效或已过期：{}", realToken);
            return handleUnauthorized(response, "Token无效或已过期，请重新登录");
        }
    }

    /**
     * 后置处理：清除用户上下文，防止ThreadLocal内存泄漏
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
        log.debug("清除用户上下文，请求：{}", request.getRequestURI());
    }

    /**
     * 统一处理未授权响应
     */
    private boolean handleUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Result<Object> result = Result.unauthorized(message);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(result));
            writer.flush();
        }

        return false;
    }
}