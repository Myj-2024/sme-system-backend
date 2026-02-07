package com.sme.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 验证BCrypt加密串对应的原始密码
 */
public class PasswordCheckTest {
    public static void main(String[] args) {
        // 数据库中的加密密码
        String encodedPassword = "$2a$10$jB.dKkq4AbJTP96HCDD3Q.4BwgFoDLJjAhaCfqLa1TmMv3QeipK4K";

        // 待验证的原始密码
        String[] testPasswords = {"123456", "admin", "12345678", "password", "root"};

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 逐个验证
        for (String rawPwd : testPasswords) {
            boolean match = encoder.matches(rawPwd, encodedPassword);
            System.out.println("原始密码：" + rawPwd + " → 是否匹配：" + match);
        }
    }
}