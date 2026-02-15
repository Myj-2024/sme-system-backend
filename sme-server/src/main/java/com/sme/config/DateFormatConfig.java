package com.sme.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 全局日期时间格式化配置，适配前端的 yyyy-MM-dd HH:mm:ss 格式
 */
@Configuration
public class DateFormatConfig {

    // 匹配你前端formatDate工具函数的格式
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // 序列化：LocalDateTime -> String（返回给前端时）
            builder.serializerByType(
                    LocalDateTime.class,
                    new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT))
            );
            // 反序列化：String -> LocalDateTime（接收前端参数时）
            builder.deserializerByType(
                    LocalDateTime.class,
                    new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT))
            );
        };
    }
}
