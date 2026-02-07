package com.sme.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Component
public class JwtUtil {

    // JWT秘钥（建议放到配置文件中）
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // JWT过期时间（24小时）
    private static final long EXPIRATION = 86400000;

    /**
     * 生成JWT令牌
     */
    public static String generateToken(Long id, String username) {
        // 当前时间
        Date now = new Date();
        // 过期时间
        Date expirationDate = new Date(now.getTime() + EXPIRATION);

        // 自定义声明
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("username", username);

        // 构建JWT
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * 解析JWT令牌
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证JWT令牌有效性
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            // 额外检查是否过期
            Claims claims = parseToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从令牌中获取用户ID
     */
    public static Long getIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("id", Long.class);
    }

    /**
     * 从令牌中获取用户名
     */
    public static String getUserNameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }
}