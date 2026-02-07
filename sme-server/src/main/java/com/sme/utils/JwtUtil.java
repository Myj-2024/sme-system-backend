package com.sme.utils;

import com.sme.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类（修复版）
 * 解决静态方法/实例注入混用、算法错误、异常处理缺失问题
 */
@Slf4j
@Component
public class JwtUtil {

    // 改为实例变量，避免静态导致的注入问题
    private final JwtConfig jwtConfig;
    private SecretKey secretKey;

    // 构造器注入（Spring推荐方式）
    @Autowired
    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        // 初始化秘钥
        this.initSecretKey();
    }

    // 初始化秘钥（实例方法）
    private void initSecretKey() {
        try {
            // 校验密钥长度（HS256至少32字节）
            byte[] keyBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
            if (keyBytes.length < 32) {
                log.warn("JWT密钥长度不足32字节，已自动补全");
                byte[] newKey = new byte[32];
                System.arraycopy(keyBytes, 0, newKey, 0, Math.min(keyBytes.length, 32));
                this.secretKey = Keys.hmacShaKeyFor(newKey);
            } else {
                this.secretKey = Keys.hmacShaKeyFor(keyBytes);
            }
        } catch (Exception e) {
            log.error("JWT秘钥初始化失败", e);
            throw new RuntimeException("JWT秘钥初始化失败", e);
        }
    }

    /**
     * 生成JWT令牌（实例方法）
     */
    public String generateToken(Long id, String username) {
        try {
            if (id == null || username == null) {
                throw new IllegalArgumentException("用户ID和用户名不能为空");
            }

            Date now = new Date();
            // 过期时间（配置文件中的expiration是秒，转成毫秒）
            Date expirationDate = new Date(now.getTime() + jwtConfig.getExpiration() * 1000L);

            Map<String, Object> claims = new HashMap<>();
            claims.put("id", id);
            claims.put("username", username);

            // 修复：使用正确的HS256算法（ES512是椭圆曲线算法，不兼容HMAC密钥）
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(expirationDate)
                    .signWith(secretKey, SignatureAlgorithm.HS256) // 核心修复点
                    .compact();
        } catch (Exception e) {
            log.error("生成Token失败：{}", e.getMessage(), e);
            throw new RuntimeException("生成Token失败", e);
        }
    }

    /**
     * 解析JWT令牌（实例方法）
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("解析Token失败：{}", e.getMessage());
            throw new RuntimeException("解析Token失败", e);
        }
    }

    /**
     * 验证JWT令牌有效性（实例方法）
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            // 只解析一次，提升性能
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            log.warn("Token验证失败：{}", e.getMessage());
            return false;
        }
    }

    /**
     * 从令牌中获取用户ID（实例方法）
     */
    public Long getIdFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.get("id", Long.class);
        } catch (Exception e) {
            log.error("获取用户ID失败：{}", e.getMessage());
            throw new RuntimeException("获取用户ID失败", e);
        }
    }

    /**
     * 从令牌中获取用户名（实例方法）
     */
    public String getUserNameFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.get("username", String.class);
        } catch (Exception e) {
            log.error("获取用户名失败：{}", e.getMessage());
            throw new RuntimeException("获取用户名失败", e);
        }
    }

    /**
     * 刷新令牌（实例方法）
     */
    public String refreshToken(String token) {
        try {
            if (!validateToken(token)) {
                throw new RuntimeException("原Token无效，无法刷新");
            }

            Long id = getIdFromToken(token);
            String username = getUserNameFromToken(token);
            return generateToken(id, username);
        } catch (Exception e) {
            log.error("刷新Token失败：{}", e.getMessage());
            throw new RuntimeException("刷新Token失败", e);
        }
    }

    /**
     * 检查令牌是否即将过期
     */
    public Boolean isTokenExpiredSoon(String token) {
        try {
            Claims claims = parseToken(token);
            Date expiration = claims.getExpiration();
            Date now = new Date();
            // 剩余时间小于1小时
            return (expiration.getTime() - now.getTime()) < 3600000L;
        } catch (Exception e) {
            log.error("检查Token过期状态失败：{}", e.getMessage());
            return true;
        }
    }
}