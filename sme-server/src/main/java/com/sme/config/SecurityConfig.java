package com.sme.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用 CSRF（API 场景下无需 CSRF）
                .csrf(AbstractHttpConfigurer::disable)
                // 2. 禁用 HTTP Basic 认证
                .httpBasic(AbstractHttpConfigurer::disable)
                // 3. 禁用表单登录
                .formLogin(AbstractHttpConfigurer::disable)
                // 4. 禁用注销功能
                .logout(AbstractHttpConfigurer::disable)
                // 5. 配置会话策略为无状态（REST API 最佳实践）
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 6. 授权规则
                .authorizeHttpRequests(auth -> auth
                        // 放行登录接口
                        .requestMatchers("/admin/auth/login").permitAll()


                        // 放行 Swagger 文档接口（如果需要）
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/readme.html").permitAll()
                        // 其他所有请求都允许（实际认证由 JWT 拦截器处理）
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    /**
     * 密码编码器 Bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}