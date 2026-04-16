# 问卷调查系统

一个功能完整的问卷调查系统，包含手机端问卷填写页面和PC端管理后台。

## 功能特点

- **手机问卷填写页**: 移动端优化的问卷填写界面
- **PC管理后台**: 使用 Element Plus UI 的功能丰富的管理界面
- **题型支持**: 单选题、多选题、下拉框、文本框
- **图片支持**: 问题支持显示图片
- **双模式**: 演示模式（静态JSON）和 API 模式（后端服务）
- **数据统计**: 实时回答统计和数据导出

## 技术栈

**前端**: Vue 3, Tailwind CSS, Element Plus, Pinia, Vue Router

**后端**: Spring Boot 3.x, MyBatis-Plus, Spring Security + JWT, MySQL, Redis

## 快速开始

### 前端（演示模式）

```bash
cd survey-system
npm install
npm run dev
```

### 后端（完整模式）

**环境要求**: JDK 17+, MySQL 8.0+, Redis（可选）

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
export const DEMO_MODE = false
export const API_BASE_URL = 'http://localhost:8080/api'
```

## 项目结构

```
├── survey-system/          # 前端项目 (Vue 3)
│   ├── src/
│   │   ├── api/           # API服务层
│   │   ├── components/     # Vue组件
│   │   ├── views/          # 页面视图
│   │   ├── stores/         # Pinia状态管理
│   │   └── config.js       # 配置文件
│
└── survey-backend/          # 后端项目 (Spring Boot)
    └── src/main/java/com/survey/
        ├── controller/       # REST控制器
        ├── service/          # 业务逻辑
        ├── mapper/           # MyBatis Mapper
        └── entity/           # 数据库实体
```

## API文档

访问: http://localhost:8080/api/doc.html

## 默认账号

- 用户名: `admin`
- 密码: `admin123`

## 许可证

MIT
