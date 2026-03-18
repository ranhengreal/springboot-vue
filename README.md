# 🔐 SpringBoot3 + Vue3 权限管理系统脚手架

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.3-4fc08d.svg)](https://vuejs.org/)
[![MyBatis Plus](https://img.shields.io/badge/MyBatis%20Plus-3.5-orange.svg)](https://baomidou.com/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

## 📋 项目简介

这是一个基于 **SpringBoot 3.x** 和 **Vue 3.x** 构建的通用后台管理系统脚手架。它实现了基础的权限管理功能，可以作为你快速开启新项目或毕业设计的坚实基础。

### ✨ 功能特性

- ✅ **用户管理**：用户的增删改查、状态管理
- ✅ **角色管理**：角色的增删改查、状态管理
- ✅ **权限管理**：菜单/按钮级别权限的增删改查，树形结构展示
- ✅ **角色授权**：为角色分配不同的权限
- ✅ **JWT 鉴权**：基于 Token 的身份认证，保障接口安全
- ✅ **动态路由与按钮**：前端根据用户权限动态生成路由和按钮
- ✅ **通用功能**：统一返回结果、全局异常处理、文件上传下载、Excel 导入导出

## 🛠️ 技术栈

### 后端
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.x | 核心框架 |
| MyBatis Plus | 3.5.x | ORM框架 |
| MySQL | 8.0+ | 数据库 |
| JWT | 0.12.x | 认证机制 |
| Hutool | 5.8.x | 工具库 |
| Apache POI | 5.4.x | Excel处理 |

### 前端
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.3.x | 前端框架 |
| Vite | 5.0.x | 构建工具 |
| Element Plus | 2.4.x | UI组件库 |
| Pinia | 2.1.x | 状态管理 |
| Vue Router | 4.2.x | 路由管理 |
| Axios | 1.6.x | HTTP客户端 |

## 🚀 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- Node.js 18+
- MySQL 8.0+
- Git

### 获取项目
```bash
git clone https://github.com/ranhengreal/springboot-vue.git
cd springboot-vue

创建数据库
CREATE DATABASE IF NOT EXISTS `auth_db_02` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

执行初始化脚本
sql/auth_db_02.sql

修改数据库配置
cd auth-api
# 编辑 src/main/resources/application.yml
# 修改数据库用户名和密码

启动服务
# Windows
mvn spring-boot:run

# Linux/Mac
./mvnw spring-boot:run

前端启动
1.安装依赖
cd auth-vue
npm install
2.修改环境配置（如果需要）
# 编辑 .env.development 文件
VITE_APP_API_BASE_URL = 'http://localhost:8080'
3.启动开发服务器
启动开发服务器

前端服务默认运行在 http://localhost:5173

访问系统
浏览器打开 http://localhost:5173
默认账号：admin / 123456

项目结构
springboot-vue/
├── auth-api/                # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/hezheng/authapi/
│   │   │   │       ├── common/        # 通用模块（异常处理、结果封装）
│   │   │   │       ├── config/         # 配置类
│   │   │   │       ├── controller/     # 控制器层
│   │   │   │       ├── entity/         # 实体类
│   │   │   │       ├── mapper/         # 数据访问层
│   │   │   │       ├── service/        # 业务逻辑层
│   │   │   │       └── utils/          # 工具类
│   │   │   └── resources/
│   │   │       ├── mapper/            # MyBatis XML文件
│   │   │       └── application.yml     # 配置文件
│   │   └── test/                       # 测试代码
│   └── pom.xml                         # Maven配置
│
├── auth-vue/                # 前端项目
│   ├── public/              # 静态资源
│   ├── src/
│   │   ├── api/             # API接口
│   │   ├── assets/          # 图片等资源
│   │   ├── components/       # 公共组件
│   │   ├── directive/       # 自定义指令
│   │   ├── layout/          # 布局组件
│   │   ├── router/          # 路由配置
│   │   ├── store/           # Pinia状态管理
│   │   ├── utils/           # 工具类
│   │   └── views/           # 页面视图
│   ├── .env                 # 环境变量
│   ├── package.json         # 依赖配置
│   └── vite.config.js       # Vite配置
│
├── sql/                     # 数据库脚本
│   └── auth_db_02.sql       # 初始化脚本
│
└── README.md                # 项目说明文档


权限说明
权限编码	说明
user:add	添加用户
user:edit	编辑用户
user:delete	删除用户
user:import	导入用户
user:export	导出用户
role:add	添加角色
role:edit	编辑角色
role:delete	删除角色
role:assign	角色授权
permission:add	添加权限
permission:edit	编辑权限
permission:delete	删除权限

联系方式
项目地址：https://github.com/ranhengreal/springboot-vue

作者：ranhengreal

如果这个项目对你有帮助，欢迎 Star ⭐ 支持！