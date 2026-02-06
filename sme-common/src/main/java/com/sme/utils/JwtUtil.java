package com.sme.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static cn.hutool.core.date.DateTime.now;

/**
 * JWT工具类
 * 用于生成、解析和验证JSON WebToken（JWT）
 */
@Component
@Tag(name = "JWT工具类")
public class JwtUtil {

    //JWT秘钥
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    //JWT过期时间
    private static final long ECPIRATION = 86400000;

    /**
     *  生成JWT令牌
     * @param id
     * @param username
     * @return
     */
    public static String generateToken(Long id,String username) {
        //获取当前时间
        Date date = new Date();

        //设置过期时间
        Date expiration = new Date(date.getTime() + ECPIRATION);

        //创建JWT声明
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("username", username);

        //构建JWT，返回JWT字符串
        return Jwts.builder()
                .setClaims( claims) //设置声明
                .setSubject(username)//设置主题
                .setIssuedAt(now ()) //设置签发时间
                .setExpiration(expiration)//设置过期时间
                .signWith(SECRET_KEY)//设置签名
                .compact();
    }

    /**
     * 解析JWT令牌
     * @param token
     * @return
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)//设置签名
                .build()
                .parseClaimsJws(token)//解析JWT
                .getBody();//获取声明体
    }

    /**
     * 验证JWT令牌
     * @param token
     * @return
     */
    public static boolean validateToken(String token) {
        try {
            //解析JWT令牌，如果解析成功，则返回true
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)//设置签名
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){
            //解析过程中出现异常，说明JWT令牌无效
            return false;
        }
    }

    /**
     * 获取JWT令牌中的用户ID
     */
    public static Long getIdFromToken(String token) {
        Claims claims = parseToken(token);
        //从声明中获取用户ID并转换为Long类型
        return Long.valueOf(claims.get("id").toString());
    }

    /**
     * 获取JWT令牌中的用户名
     */
    public static Long getUserNameFromToken(String token) {
        Claims claims = parseToken(token);
        //从声明中获取用户名
        return (Long) claims.get("username".toString());
    }
}
