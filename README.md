[前端仓库](https://github.com/Myj-2024/sme-system-frontend)

## 项目简介

基于 Spring Boot 3.x 构建的中小企业管理系统后端服务，提供数据持久化、接口认证、文件存储、分页等核心能力，适配 MySQL 数据库与 MinIO 对象存储。

## 技术栈

- 核心框架：Spring Boot 3.x
- 数据持久化：MyBatis + PageHelper
- 数据库：MySQL 8.0+
- 认证授权：JWT
- 接口文档：SpringDoc (OpenAPI 3.1)
- 文件存储：MinIO
- 连接池：HikariCP

## 快速启动

### 环境准备

1. JDK 17+
2. MySQL 8.0+（创建数据库 `gh_sme_service`）
3. MinIO 服务（配置对应桶 `my-project`）

### 运行步骤

1. 克隆项目：

```bash
git clone https://github.com/Myj-2024/sme-system-backend.git
cd sme-system-backend
```

2. 修改 `application.yml` 中数据库、MinIO 配置（地址/账号/密码）；
3. 编译运行：

```bash
mvn clean package
java -jar target/gh-sme-sys.jar
```

4. 访问接口文档：http://localhost:8080/swagger-ui/index.html

## 核心配置

| 配置项       | 关键信息                                   |
| ------------ | ------------------------------------------ |
| 服务端口     | 8080                                       |
| 数据库       | jdbc:mysql://localhost:3306/gh_sme_service |
| JWT 有效期   | 24小时（86400秒）                          |
| MinIO 端点   | http://192.168.43.20:19966                 |
| 文件上传限制 | 单文件100MB，单次请求200MB                 |

