package com.sme.config;

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
                // 1. 配置接口文档基本信息（标题、描述、版本、联系人、协议）
                .info(getApiInfo())
                // 2. 配置多环境服务器（开发、测试）
                .servers(getServers())
                // 3. 配置外部文档（如部署说明、接口文档）
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
                .description("基于Java17+SpringBoot4.0+MyBatis+Spring Data JDBC开发的包抓联管理系统，包含企业、包抓联、字典等核心接口")
                // 联系人信息（你的信息）
                .contact(new Contact()
                        .name("马宇杰")
                        .email("3287456080@qq.com")
                        .url("https://www.sme.com"))
                // 服务条款（可选）
                .termsOfService("https://www.sme.com/terms")
                // 开源协议（可选，如Apache2.0）
                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                // 接口版本
                .version("1.0.0");
    }

    /**
     * 配置多环境服务器（开发、测试，生产环境可后续添加）
     */
    private List<Server> getServers() {
        // 开发环境
        Server devServer = new Server()
                .url("http://localhost:8080/") // 注意：包含项目context-path=/sme，否则接口调试会404
                .description("开发环境（本地）");
        // 测试环境
        Server testServer = new Server()
                .url("https://test.sme.com/")
                .description("测试环境（远程）");
        // 返回服务器列表
        return List.of(devServer, testServer);
    }

    /**
     * 配置外部文档（如部署说明、数据库设计文档）
     */
    private ExternalDocumentation getExternalDocs() {
        return new ExternalDocumentation()
                .description("广河中小微企业服务系统-数据库设计文档")
                .url("http://localhost:8080/readme.html"); // 包含context-path
    }
}
