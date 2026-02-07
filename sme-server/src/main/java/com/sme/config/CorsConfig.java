package com.sme.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置类
 */
@Configuration
public class CorsConfig {

    /**
     * 创建CorsFilter过滤器Bean
     * 这个过滤器用于处理跨域请求
     * @return
     */
    @Bean
    public CorsFilter corsFilter(){
        //创建跨域配置对象
        CorsConfiguration config = new CorsConfiguration();

        //允许所有源进行跨域请求
        config.addAllowedOriginPattern("http://localhost:5173");

        //允许所有请求方法进行跨域请求
        config.addAllowedHeader("*");

        //允许哪些HTTP方法进行跨域请求
        config.addAllowedMethod( "*");

        //是否允许携带认证 信息
        config.setAllowCredentials(true);

        //预检请求的缓存时间
        config.setMaxAge(3600L);

        //创建配置源，将上面的所有配置添加到配置源中
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        //将配置映射到所有路径
        source.registerCorsConfiguration("/**", config);

        //创建CorsFilter过滤器对象，并返回
        return new CorsFilter(source);
    }
}
