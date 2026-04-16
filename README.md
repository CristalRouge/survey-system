# 问卷调查系统 / Survey System

[English](#english) | [中文](#中文)

---

## English

A full-stack survey system with mobile-friendly questionnaire filling and PC admin dashboard.

### Features

- **Mobile Questionnaire Page**: Optimized for mobile users with touch-friendly interface
- **PC Admin Dashboard**: Feature-rich management interface using Element Plus UI
- **Question Types**: Single choice, multiple choice, dropdown, text input
- **Image Support**: Display images in questions
- **Dual Mode**: Demo mode with static JSON data or API mode with backend
- **Statistics**: Real-time response statistics and data export

### Tech Stack

**Frontend**
- Vue 3 + JavaScript
- Tailwind CSS
- Element Plus (Admin UI)
- Pinia (State Management)
- Vue Router

**Backend**
- Spring Boot 3.x
- MyBatis-Plus
- Spring Security + JWT
- MySQL
- Redis

### Quick Start

#### Frontend (Demo Mode)

```bash
cd survey-system
npm install
npm run dev
```

Access: http://localhost:5173

#### Backend (Full Stack)

**Prerequisites**
- JDK 17+
- MySQL 8.0+
- Redis (optional)

**Setup Database**
```bash
mysql -u root -p < survey-backend/src/main/resources/schema.sql
mysql -u root -p < survey-backend/src/main/resources/data.sql
```

**Start Backend**
```bash
cd survey-backend
mvn spring-boot:run
```

**Update Frontend Config**
Edit `survey-system/src/config.js`:
```javascript
export const DEMO_MODE = false  // Switch to API mode
export const API_BASE_URL = 'http://localhost:8080/api'
```

### Project Structure

```
├── survey-system/          # Frontend (Vue 3)
│   ├── src/
│   │   ├── api/           # API service layer
│   │   ├── components/    # Vue components
│   │   ├── views/         # Page views
│   │   ├── stores/        # Pinia stores
│   │   └── config.js       # Configuration
│   └── package.json
│
└── survey-backend/          # Backend (Spring Boot)
    └── src/main/java/com/survey/
        ├── controller/      # REST controllers
        ├── service/         # Business logic
        ├── mapper/          # MyBatis mappers
        └── entity/          # Database entities
```

### API Documentation

When backend is running, visit: http://localhost:8080/api/doc.html

### Default Account

- Username: `admin`
- Password: `admin123`

### License

MIT

---

## 中文

一个功能完整的问卷调查系统，包含手机端问卷填写页面和PC端管理后台。

### 功能特点

- **手机问卷填写页**: 移动端优化的问卷填写界面
- **PC管理后台**: 使用 Element Plus UI 的功能丰富的管理界面
- **题型支持**: 单选题、多选题、下拉框、文本框
- **图片支持**: 问题支持显示图片
- **双模式**: 演示模式（静态JSON）和 API 模式（后端服务）
- **数据统计**: 实时回答统计和数据导出

### 技术栈

**前端**
- Vue 3 + JavaScript
- Tailwind CSS
- Element Plus (管理界面UI)
- Pinia (状态管理)
- Vue Router

**后端**
- Spring Boot 3.x
- MyBatis-Plus
- Spring Security + JWT
- MySQL
- Redis

### 快速开始

#### 前端（演示模式）

```bash
cd survey-system
npm install
npm run dev
```

访问: http://localhost:5173

#### 后端（完整模式）

**环境要求**
- JDK 17+
- MySQL 8.0+
- Redis（可选）

**初始化数据库**
```bash
mysql -u root -p < survey-backend/src/main/resources/schema.sql
mysql -u root -p < survey-backend/src/main/resources/data.sql
```

**启动后端**
```bash
cd survey-backend
mvn spring-boot:run
```

**切换到API模式**
修改 `survey-system/src/config.js`:
```javascript
export const DEMO_MODE = false  // 切换到API模式
export const API_BASE_URL = 'http://localhost:8080/api'
```

### 项目结构

```
├── survey-system/          # 前端项目 (Vue 3)
│   ├── src/
│   │   ├── api/           # API服务层
│   │   ├── components/     # Vue组件
│   │   ├── views/         # 页面视图
│   │   ├── stores/        # Pinia状态管理
│   │   └── config.js       # 配置文件
│   └── package.json
│
└── survey-backend/            # 后端项目 (Spring Boot)
    └── src/main/java/com/survey/
        ├── controller/      # REST控制器
        ├── service/         # 业务逻辑
        ├── mapper/          # MyBatis Mapper
        └── entity/          # 数据库实体
```

### API文档

后端启动后访问: http://localhost:8080/api/doc.html

### 默认账号

- 用户名: `admin`
- 密码: `admin123`

### 许可证

MIT
