---
AIGC:
    ContentProducer: Minimax Agent AI
    ContentPropagator: Minimax Agent AI
    Label: AIGC
    ProduceID: d36d55f06a5f93dc7c7aa40954a61966
    PropagateID: d36d55f06a5f93dc7c7aa40954a61966
    ReservedCode1: 30460221009222045635d51aa7da9fcb731769c2f19e8d97336ffc439385c1d09be234fe2b022100fe8a61a4671ab5b766d5561161aebdbde7f43620b38b08aa3a4aa8b330de43b7
    ReservedCode2: 3044022060ea42d77fa5ca0c4dbb5772fc5156f6bbd3fc076a4287c4292aab11a10313b302207d5fdf24b94daac9b1428106055598babc2e57d9e178947de526387f30ab45c0
---

# 问卷调查系统规范

## 1. 概念与愿景

一个现代化、功能完整的问卷调查平台，为用户提供流畅的问卷填写体验，同时为管理者提供强大的问卷配置功能。系统设计强调简洁、专业和易用性，界面风格清新现代，交互反馈及时明确。

## 2. 设计语言

### 美学方向
采用现代简约风格，以大量留白和清晰的信息层次营造专业的问卷体验。配色以蓝色为主调，传递信任和专业的感受。

### 色彩系统
- **主色 (Primary)**: `#3B82F6` - 明亮蓝色，用于主要按钮和交互元素
- **次色 (Secondary)**: `#64748B` - 石板灰，用于次要文本和边框
- **强调色 (Accent)**: `#10B981` - 翠绿色，用于成功状态和进度指示
- **背景色**: `#F8FAFC` - 极浅灰蓝，用于页面背景
- **卡片背景**: `#FFFFFF` - 纯白色，用于内容卡片
- **文字主色**: `#1E293B` - 深灰蓝，用于主要文字
- **文字次色**: `#64748B` - 中灰，用于次要文字
- **错误色**: `#EF4444` - 红色，用于错误提示
- **警告色**: `#F59E0B` - 琥珀色，用于警告提示

### 字体系统
- **中文字体**: "PingFang SC", "Microsoft YaHei", sans-serif
- **英文字体**: "Inter", -apple-system, sans-serif
- **代码字体**: "JetBrains Mono", monospace
- **标题**: 700 weight, 1.2 line-height
- **正文**: 400 weight, 1.6 line-height

### 空间系统
- 基础单位: 4px
- 常用间距: 8px, 12px, 16px, 24px, 32px, 48px
- 卡片圆角: 12px
- 按钮圆角: 8px
- 输入框圆角: 8px

### 动效设计
- 过渡时长: 200ms ease-out
- 悬停效果: 轻微上浮 + 阴影加深
- 页面切换: 淡入淡出
- 加载状态: 脉冲动画

## 3. 布局与结构

### 页面架构

#### 移动端问卷填写页
- 顶部: 问卷标题和描述
- 进度条: 显示当前完成进度
- 问题列表: 垂直滚动，每个问题独立卡片
- 底部: 固定提交按钮
- 响应式: 单列布局，padding: 16px

#### PC管理页
- 左侧: 导航菜单 (固定)
- 右侧: 主内容区域
- 问卷列表: 表格展示
- 编辑页面: 左右分栏 (问题列表 + 编辑区)
- 响应式: 最大宽度 1440px，居中显示

### 路由设计
- `/` - 首页，展示可填写的问卷列表
- `/survey/:id` - 问卷填写页面
- `/admin` - 管理后台首页
- `/admin/survey/:id` - 问卷编辑页面
- `/admin/survey/create` - 创建新问卷
- `/admin/responses/:id` - 问卷回答统计

## 4. 功能与交互

### 问卷填写页面 (移动端优化)
- **单选题**: 单选按钮组，点击选中，选中项高亮
- **多选题**: 复选框组，可多选，选中项高亮
- **下拉框**: 原生select美化，点击展开选项
- **文本框**: 多行文本输入，支持自适应高度
- **图片显示**: 问题描述中的图片居中展示，最大宽度100%
- **必填标记**: 必填问题显示红色星号
- **进度保存**: 本地存储当前进度
- **表单验证**: 提交前检查必填项
- **成功反馈**: 提交后显示感谢页面

### 管理页面 (PC端)
- **问卷列表**:
  - 显示问卷标题、状态、创建时间、回答数
  - 支持搜索和筛选
  - 快速操作: 编辑、预览、删除、复制链接
- **问卷编辑**:
  - 拖拽排序问题
  - 添加/删除/编辑问题
  - 问题类型切换
  - 选项的增删改
  - 图片上传 (使用URL输入)
  - 必填设置
- **数据统计**:
  - 每个问题的回答分布
  - 饼图/柱状图展示
  - 导出Excel功能

### 交互细节
- **按钮悬停**: 背景色加深 10%，轻微上浮
- **输入聚焦**: 边框变为主色，添加阴影
- **删除确认**: 弹出确认对话框
- **保存提示**: 操作后显示成功Toast
- **加载状态**: 按钮显示加载动画
- **错误提示**: 输入框下方显示错误信息

## 5. 组件清单

### 问卷组件
| 组件 | 状态 | 描述 |
|------|------|------|
| QuestionCard | default, answered, error | 问题卡片容器 |
| SingleChoice | unchecked, checked, disabled | 单选题选项 |
| MultipleChoice | unchecked, checked, disabled | 多选题选项 |
| DropdownSelect | closed, open, disabled | 下拉选择框 |
| TextInput | empty, focused, filled, error, disabled | 文本输入框 |
| ImageDisplay | loading, loaded, error | 图片展示组件 |
| ProgressBar | - | 进度条组件 |
| SubmitButton | idle, loading, disabled | 提交按钮 |

### 管理组件
| 组件 | 状态 | 描述 |
|------|------|------|
| SurveyTable | loading, loaded, empty | 问卷列表表格 |
| QuestionEditor | single, multiple, dropdown, text | 问题编辑表单 |
| OptionItem | default, editing | 选项编辑项 |
| ImageUploader | empty, uploading, uploaded, error | 图片上传组件 |
| ConfirmDialog | - | 确认对话框 |
| Toast | success, error, warning | 提示消息 |
| TabNav | - | 标签导航 |

## 6. 技术方案

### 技术栈
- **框架**: Vue 3 (Composition API)
- **构建工具**: Vite
- **样式**: Tailwind CSS
- **UI组件**: Element Plus (仅管理页面)
- **路由**: Vue Router 4
- **状态管理**: Pinia
- **图表**: Chart.js

### 数据模型

```typescript
// 问卷
interface Survey {
  id: string;
  title: string;
  description: string;
  coverImage?: string;
  status: 'draft' | 'published' | 'closed';
  questions: Question[];
  createdAt: string;
  updatedAt: string;
  responseCount: number;
}

// 问题
interface Question {
  id: string;
  type: 'single' | 'multiple' | 'dropdown' | 'text';
  title: string;
  description?: string;
  image?: string;
  required: boolean;
  options?: Option[];
  placeholder?: string;
  minLength?: number;
  maxLength?: number;
}

// 选项
interface Option {
  id: string;
  text: string;
  image?: string;
}

// 回答
interface Response {
  id: string;
  surveyId: string;
  answers: Answer[];
  submittedAt: string;
}

// 答案
interface Answer {
  questionId: string;
  value: string | string[];
}
```

### 静态数据
使用本地JSON文件存储演示数据，包含:
- 3份示例问卷
- 每份问卷包含4-6个问题
- 问题类型覆盖所有支持的类型
- 包含带图片的问题示例

## 7. 文件结构

```
/survey-system
├── index.html
├── package.json
├── vite.config.js
├── tailwind.config.js
├── postcss.config.js
├── src/
│   ├── main.js
│   ├── App.vue
│   ├── router/
│   │   └── index.js
│   ├── stores/
│   │   └── survey.js
│   ├── views/
│   │   ├── HomePage.vue
│   │   ├── SurveyFill.vue
│   │   ├── SurveySuccess.vue
│   │   ├── AdminDashboard.vue
│   │   ├── SurveyEditor.vue
│   │   └── ResponseStats.vue
│   ├── components/
│   │   ├── survey/
│   │   │   ├── QuestionCard.vue
│   │   │   ├── SingleChoice.vue
│   │   │   ├── MultipleChoice.vue
│   │   │   ├── DropdownSelect.vue
│   │   │   ├── TextInput.vue
│   │   │   ├── ImageDisplay.vue
│   │   │   └── ProgressBar.vue
│   │   └── admin/
│   │       ├── SurveyTable.vue
│   │       ├── QuestionEditor.vue
│   │       ├── OptionEditor.vue
│   │       └── ImageUploader.vue
│   └── data/
│       └── surveys.json
└── dist/
```
