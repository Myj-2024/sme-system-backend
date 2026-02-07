package com.sme.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 当前用户工具类（修复版）
 * 适配实例化的JwtUtil，优化性能，增加异常处理和日志
 */
@Slf4j
@Component // 改为Spring Bean，支持注入JwtUtil
public class CurrentUser {

    // 注入实例化的JwtUtil
    private static JwtUtil jwtUtil;

    // 静态注入（解决工具类中注入Spring Bean的问题）
    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        CurrentUser.jwtUtil = jwtUtil;
    }

    /**
     * 公共方法：从请求中提取纯Token
     */
    private static String getPureTokenFromRequest() {
        // 获取当前请求属性
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes attributes)) {
            log.warn("无法获取当前请求上下文");
            return null;
        }

        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");

        // 校验Token格式
        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
            log.warn("Token不存在或格式错误：{}", token);
            return null;
        }

        // 提取纯Token并去空格
        String pureToken = token.substring(7).trim();
        if (!StringUtils.hasText(pureToken)) {
            log.warn("Token内容为空");
            return null;
        }

        return pureToken;
    }

    /**
     * 获取当前登录用户ID（优先从UserContext获取，提升性能）
     * @return 用户ID / null
     */
    public static Long getCurrentUserId() {
        try {
            // 优先从UserContext获取（拦截器已解析，避免重复解析Token）
            Long userId = UserContext.getUserId();
            if (userId != null) {
                return userId;
            }

            // UserContext中无数据时，解析Token获取
            String pureToken = getPureTokenFromRequest();
            if (pureToken == null) {
                return null;
            }

            // 使用实例化的JwtUtil解析（核心修复）
            userId = jwtUtil.getIdFromToken(pureToken);
            log.debug("从Token解析出用户ID：{}", userId);
            return userId;
        } catch (Exception e) {
            log.error("获取当前用户ID失败", e);
            return null;
        }
    }

    /**
     * 获取当前登录用户名（优先从UserContext获取）
     * @return 用户名 / null
     */
    public static String getCurrentUsername() {
        try {
            // 优先从UserContext获取
            String username = UserContext.getUsername();
            if (username != null) {
                return username;
            }

            // UserContext中无数据时，解析Token获取
            String pureToken = getPureTokenFromRequest();
            if (pureToken == null) {
                return null;
            }

            // 使用实例化的JwtUtil解析（核心修复）
            username = jwtUtil.getUserNameFromToken(pureToken);
            log.debug("从Token解析出用户名：{}", username);
            return username;
        } catch (Exception e) {
            log.error("获取当前用户名失败", e);
            return null;
        }
    }
}