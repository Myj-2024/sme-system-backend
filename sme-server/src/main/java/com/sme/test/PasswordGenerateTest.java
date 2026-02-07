package com.sme.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerateTest {
    public static void main(String[] args) {
        String rawPassword = "123456";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("123456 的加密密码：" + encodedPassword);
    }
}
