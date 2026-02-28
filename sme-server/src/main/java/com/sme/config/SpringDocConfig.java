package com.sme.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.ExternalDocumentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Spring Doc配置类（适配SpringBoot4.0+、OpenAPI3.0）
 * 存放位置：server模块 → 业务配置，仅server模块加载
 */
@Configuration
public class SpringDocConfig {

    /**
     * 核心Bean：自定义OpenAPI全量配置
     * 包含：文档基本信息、服务器环境、外部文档、协议等
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                // 1. 必须声明 OpenAPI 版本
                .openapi("3.0.1")
                // 2. 配置接口文档基本信息
                .info(getApiInfo())
                // 3. 配置多环境服务器
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("开发环境服务器")
//                        new Server().url("https://test.sme.com").description("测试环境服务器")
                ))
                // 4. 配置外部文档
                .externalDocs(getExternalDocs());
    }

    /**
     * 配置文档基本信息
     */
    private Info getApiInfo() {
        return new Info()
                // 文档标题
                .title("广河中小微企业服务系统API文档")
                // 文档描述
                .description("广河中小微企业服务系统核心接口")
                // 联系人信息
                .contact(new Contact()
                        .name("马宇杰")
                        .email("3287456080@qq.com")
                        .url("https://ycdoc.de5.net"))
                // 服务条款（可选）
//                .termsOfService("https://www.sme.com/terms")
                // 开源协议（可选，如Apache2.0）
                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                // 接口版本
                .version("1.0.0");
    }

    /**
     * 配置外部文档
     */
    private ExternalDocumentation getExternalDocs() {
        return new ExternalDocumentation()
                .description("数据库设计文档")
                .url("http://localhost:8080/readme.html");
    }
}
