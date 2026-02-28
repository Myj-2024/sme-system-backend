package com.sme.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MyBatis 配置类（同时支持 注解式 Mapper + XML 式 Mapper）
 */
@Configuration
// 扫描 Mapper 接口包（覆盖所有 Mapper）
@MapperScan(basePackages = "com.sme.mapper")
public class MybatisConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(MybatisConfig.class);

    @Autowired
    private JwtInterceptor jwtInterceptor;

    /**
     * 添加 JWT 拦截器并配置放行路径
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("正在注册 JWT 拦截器并配置放行路径...");

        // 添加 JWT 拦截器
        registry.addInterceptor(jwtInterceptor)
                // 拦截所有以 /admin/ 开头的后台管理接口
                .addPathPatterns("/admin/**")
                // 排除不需要 Token 认证的接口（白名单）
                .excludePathPatterns(
                        "/admin/auth/login",       // 登录接口
                        "/admin/enterprise/show/list",  // 企业风采分页接口
                        "/admin/policy/page",      // 政策通告分页接口
                        "/admin/dict/item/**",     // 加载字典项
                        "/swagger-ui/**",          // Swagger 文档
                        "/v3/api-docs/**",         // API 文档
                        "/favicon.ico",
                        "/error"
                );
    }
}