package com.sme.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.io.IOException;

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
     * 添加 JWT 拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加 JWT 拦截器
        registry.addInterceptor(jwtInterceptor)
                // 拦截所有请求路径
                .addPathPatterns("/admin/**")
                // 排除登录接口
                .excludePathPatterns("/admin/auth/login");
    }

}