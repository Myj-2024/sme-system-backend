package com.sme.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
/**
 * 密码加密工具类
 */
public class PasswordEncoderUtil {

    //使用静态密码编码器实例，确保全局唯一性
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 对原始密码进行加密处理
     * @param password
     * @return 加密后的密码（哈希值）
     */
    public static String encode(String password){
        return encoder.encode(password);
    }

    /**
     * 验证密码是否匹配
     * @param rawPassword 原始密码
     * @param encodedPassword 已加密的密码
     * @return 匹配结果
     */
    public static boolean matches(String rawPassword, String encodedPassword){
        return encoder.matches(rawPassword, encodedPassword);
    }

}
