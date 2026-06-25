
# 大学成绩管理系统

基于 Spring Boot 3 + Vue 3 开发的大学成绩管理系统。

## 功能特点

- **多角色支持**: 管理员、教师、学生三种角色
- **学生管理**: 添加、编辑、删除学生信息
- **课程管理**: 添加、编辑、删除课程信息
- **成绩管理**: 录入、编辑、删除学生成绩
- **成绩查询**: 学生可以查看自己的成绩和绩点
- **数据统计**: 成绩分布统计、GPA计算
- **响应式设计**: 支持多种屏幕尺寸

## 技术栈

### 后端
- Java 21
- Spring Boot 3.2.0
- Spring Security + JWT
- Spring Data JPA
- SQLite 数据库

### 前端
- Vue 3
- Vue Router
- Element Plus
- ECharts

## 快速开始

### 环境要求
- Java 21+
- Maven 3.8+
- Node.js 20+

### 运行后端

```bash
cd backend
mvn spring-boot:run
```

### 运行前端

```bash
cd frontend
npm install
npm run dev
```

### 访问地址
- 前端: http://localhost:5173
- 后端API: http://localhost:8080

## 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 教师 | teacher001 | teacher123 |
| 学生 | 2022001 | student123 |

## Docker部署

```bash
docker-compose up --build
```

## 项目结构

```
├── backend/           # 后端代码
│   ├── src/main/java/com/example/gradebook/
│   │   ├── controller/    # REST API控制器
│   │   ├── service/       # 业务逻辑层
│   │   ├── repository/    # 数据访问层
│   │   ├── entity/        # JPA实体
│   │   ├── dto/           # 数据传输对象
│   │   ├── security/      # 安全配置
│   │   └── config/        # 配置类
│   └── pom.xml
├── frontend/          # 前端代码
│   ├── src/
│   │   ├── views/         # 页面组件
│   │   ├── api/           # API调用
│   │   ├── router/        # 路由配置
│   │   └── utils/         # 工具函数
│   └── package.json
├── Dockerfile
└── docker-compose.yml
```

## API接口

### 认证
- `POST /api/auth/login` - 登录
- `POST /api/auth/register` - 注册
- `GET /api/auth/me` - 获取当前用户

### 学生管理
- `GET /api/student/all` - 获取所有学生
- `GET /api/student/{id}` - 获取学生详情
- `POST /api/student/add` - 添加学生
- `PUT /api/student/update` - 更新学生
- `DELETE /api/student/delete/{id}` - 删除学生

### 课程管理
- `GET /api/course/all` - 获取所有课程
- `GET /api/course/{id}` - 获取课程详情
- `POST /api/course/add` - 添加课程
- `PUT /api/course/update` - 更新课程
- `DELETE /api/course/delete/{id}` - 删除课程

### 成绩管理
- `GET /api/grade/all` - 获取所有成绩
- `GET /api/grade/student/{studentId}` - 获取学生成绩
- `POST /api/grade/add` - 录入成绩
- `PUT /api/grade/update` - 更新成绩
- `DELETE /api/grade/delete/{id}` - 删除成绩
- `GET /api/grade/gpa/{studentId}` - 计算绩点
