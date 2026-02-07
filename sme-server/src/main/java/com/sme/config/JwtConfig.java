package com.sme.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置类
 * 用于读取和管理JWT的配置信息
 */
@Component
@ConfigurationProperties(prefix = "jwt")// 指定配置前缀
@Data
public class JwtConfig {

    /**
     * JWT的密钥
     */
    private String secret = "mySecretKey";

    /**
     * JWT的过期时间（毫秒）
     */
    private long expiration = 86400;

    /**
     * Http请求头中的JWT的字段名称
     */
    private String header = "Authorization";
}
