package com.sme;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@EnableTransactionManagement //开启注解方式的事务管理
@EnableCaching  //开启缓存注解功能
@EnableScheduling  //开启定时任务功能
public class SmeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmeApplication.class, args);
        System.out.println("项目启动成功\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89");
    }
}
