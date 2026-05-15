# 基于图论的反应路径网络可视化系统

## 项目概述

这是一个基于图论原理的化学反应路径网络可视化系统，用于展示和分析化合物之间的反应关系。系统提供了直观的图形化界面，支持实时的路径搜索和算法演示。

## 技术栈

### 后端
- **Spring Boot 3**: 现代化的 Java 应用框架
- **MyBatis Plus**: 强大的 ORM 框架
- **MySQL 8.0**: 关系数据库
- **JWT**: 用户身份认证
- **Maven**: 项目管理工具

### 前端
- **Vue 3**: 渐进式 JavaScript 框架
- **Vite**: 新一代前端构建工具
- **Element Plus**: 企业级 UI 组件库
- **ECharts**: 数据可视化库
- **Axios**: HTTP 客户端库
- **Pinia**: 状态管理库

### 部署
- **Docker**: 容器化部署
- **Docker Compose**: 容器编排

## 项目功能

### 1. 用户模块
- ✅ 用户登录
- ✅ JWT 认证
- ✅ 用户注册
- ✅ 用户信息获取

### 2. 化合物管理
- ✅ 新增化合物
- ✅ 编辑化合物
- ✅ 删除化合物
- ✅ 搜索化合物
- ✅ 查看化合物详情

### 3. 反应关系管理
- ✅ 新增反应路径
- ✅ 删除反应路径
- ✅ 修改反应路径
- ✅ 查看所有反应路径

### 4. 图谱可视化
- ✅ 节点展示
- ✅ 边展示
- ✅ 节点拖拽
- ✅ 缩放功能
- ✅ 路径高亮

### 5. 图论算法
- ✅ **BFS 最短路径算法**: 找到两个化合物间的最短反应路径
- ✅ **Dijkstra 最低能量路径**: 找到能量消耗最低的反应路径

## 项目结构

```
基于图论的反应路径网络可视化系统/
├── backend/                           # 后端项目
│   ├── src/
│   │   ├── main/java/com/reaction/
│   │   │   ├── ReactionApplication.java   # 主启动类
│   │   │   ├── config/                    # 配置类
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── WebMvcConfig.java
│   │   │   ├── controller/                # 控制器
│   │   │   │   ├── UserController.java
│   │   │   │   ├── CompoundController.java
│   │   │   │   └── ReactionPathController.java
│   │   │   ├── service/                   # 业务逻辑
│   │   │   │   ├── UserService.java
│   │   │   │   ├── CompoundService.java
│   │   │   │   ├── ReactionPathService.java
│   │   │   │   └── impl/
│   │   │   ├── mapper/                    # 数据访问层
│   │   │   │   ├── UserMapper.java
│   │   │   │   ├── CompoundMapper.java
│   │   │   │   └── ReactionPathMapper.java
│   │   │   ├── entity/                    # 实体类
│   │   │   │   ├── User.java
│   │   │   │   ├── Compound.java
│   │   │   │   └── ReactionPath.java
│   │   │   ├── dto/                       # 数据传输对象
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── LoginResponse.java
│   │   │   │   └── ApiResponse.java
│   │   │   ├── utils/                     # 工具类
│   │   │   │   └── JwtUtil.java
│   │   │   └── algorithm/                 # 算法实现
│   │   │       └── GraphAlgorithm.java
│   │   └── resources/
│   │       └── application.yml            # 应用配置
│   ├── pom.xml                            # Maven 配置
│   └── Dockerfile                         # Docker 配置
├── frontend/                          # 前端项目
│   ├── src/
│   │   ├── components/                    # 组件
│   │   ├── views/                         # 页面
│   │   │   ├── Login.vue
│   │   │   ├── Dashboard.vue
│   │   │   ├── Compounds.vue
│   │   │   ├── ReactionPaths.vue
│   │   │   └── Visualization.vue
│   │   ├── router/                        # 路由
│   │   │   └── index.js
│   │   ├── stores/                        # 状态管理
│   │   │   └── user.js
│   │   ├── api/                           # API 调用
│   │   │   ├── index.js
│   │   │   └── request.js
│   │   ├── utils/                         # 工具函数
│   │   ├── assets/                        # 静态资源
│   │   │   └── style.css
│   │   ├── App.vue                        # 根组件
│   │   └── main.js                        # 入口文件
│   ├── package.json                       # NPM 配置
│   ├── vite.config.js                    # Vite 配置
│   ├── index.html                         # HTML 入口
│   └── Dockerfile                         # Docker 配置
├── docker-compose.yml                 # Docker Compose 配置
├── init.sql                           # 数据库初始化脚本
└── README.md                          # 项目文档
```

## 环境要求

### Docker 模式（仅前后端）
- Docker Desktop 20.10+
- Docker Compose v2.0+

### 本地开发模式（推荐）
- Java 17 或更高版本
- Node.js 18 或更高版本
- Maven 3.6+
- MySQL 8.4（本地安装）

## 快速启动

### 方式一：本地开发模式（推荐）

#### 1. 启动 MySQL 数据库
确保本地 MySQL 8.0 已运行，并创建数据库：
```sql
-- 连接到 MySQL
mysql -u root -p

-- 创建数据库
CREATE DATABASE IF NOT EXISTS reaction_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 导入初始化数据（可选）
-- source init.sql;
```

#### 2. 启动后端服务
```bash
cd backend
mvn spring-boot:run
```

#### 3. 启动前端服务
```bash
cd frontend
npm install
npm run dev
```

### 方式二：使用 Docker Compose（仅前后端）

```bash
# 进入项目根目录
cd "d:\Users\yang\杨嘉兴\毕业设计\基于图论的反应路径网络可视化系统"

# 启动前后端服务（数据库需本地运行）
docker compose -p reaction_system up -d --build

# 查看日志
docker compose -p reaction_system logs -f
```

## 访问地址

启动成功后，访问以下地址：

- **前端界面**: http://localhost:5173
- **后端 API**: http://localhost:8080/api
- **数据库**: localhost:3306 (用户名: root, 密码: root) - 本地 MySQL 实例

---

## 📋 系统要求

### Docker 模式（仅前后端）
- Docker Desktop 20.10+
- Docker Compose v2.0+

### 本地开发模式（推荐）
- Java 17 或更高版本
- Node.js 18 或更高版本
- Maven 3.6+
- MySQL 8.4（本地安装）

---

## 🛠️ 手动启动（备选方案）

如果一键脚本无法正常工作，可以手动启动各服务：

### 1. 启动 MySQL（Docker）
```bash
docker run -d \
  --name reaction-mysql \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=reaction_system \
  mysql:8.4
```

### 2. 启动后端
```bash
cd backend
mvn spring-boot:run
```

### 3. 启动前端
```bash
cd frontend
npm install
npm run dev
```

---

## 🌐 访问地址

启动成功后，访问以下地址：

- **前端界面**: http://localhost:5173
- **后端 API**: http://localhost:8080/api
- **数据库**: localhost:3306 (用户名: root, 密码: root)

---

## 📝 常见问题

### Q1: Docker 启动失败，提示 "project name must not be empty"
**解决方案：** 脚本已自动处理，使用 `-p reaction_system` 指定项目名。

### Q2: 端口被占用
**解决方案：** 检查并关闭占用 3306、8080、5173 端口的程序。

### Q3: 前端无法连接后端
**解决方案：** 
- 确保后端已完全启动（查看后端日志）
- 检查 `frontend/.env` 或 `vite.config.js` 中的 API 地址配置

### Q4: 数据库连接失败
**解决方案：**
- Docker 模式：等待 MySQL 完全启动（约 10-20 秒）
- 本地模式：确保 MySQL 服务正在运行且已创建 `reaction_system` 数据库

---

## API 文档

### 用户模块

#### 登录
```
POST /api/users/login
Content-Type: application/json

{
  "username": "admin",
  "password": "123456"
}

Response:
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJ0eXAiOiJKV1QiLCJhbGc...",
    "userId": 1,
    "username": "admin",
    "email": "admin@example.com"
  }
}
```

#### 获取用户信息
```
GET /api/users/{userId}
Authorization: Bearer {token}

Response:
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "realName": "Administrator"
  }
}
```

### 化合物模块

#### 获取所有化合物
```
GET /api/compounds
```

#### 搜索化合物
```
GET /api/compounds/search?keyword=water
```

#### 新增化合物
```
POST /api/compounds
Content-Type: application/json

{
  "name": "Ethanol",
  "formula": "C2H5OH",
  "description": "Ethanol",
  "molarMass": 46.068,
  "energy": -277.0
}
```

### 反应路径模块

#### 获取最短路径
```
GET /api/reaction-paths/shortest-path?startId=1&endId=3
```

#### 获取最低能量路径
```
GET /api/reaction-paths/lowest-energy-path?startId=1&endId=3
```

## 图论算法说明

### BFS 最短路径算法
- **算法原理**: 使用广度优先搜索遍历图
- **时间复杂度**: O(V + E)
- **应用场景**: 找到最少反应步骤的路径

### Dijkstra 最低能量路径算法
- **算法原理**: 使用优先队列实现 Dijkstra 算法
- **时间复杂度**: O((V + E) log V)
- **应用场景**: 找到能量消耗最低的反应路径

## 数据库设计

### Users 表
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(100),
  real_name VARCHAR(100),
  status INT,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);
```

### Compounds 表
```sql
CREATE TABLE compounds (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  formula VARCHAR(100) NOT NULL,
  description TEXT,
  molar_mass DECIMAL(10, 4),
  energy DECIMAL(15, 6),
  image_url VARCHAR(500),
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);
```

### Reaction_Paths 表
```sql
CREATE TABLE reaction_paths (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  from_compound_id BIGINT NOT NULL,
  to_compound_id BIGINT NOT NULL,
  reaction_type VARCHAR(100),
  energy_change DECIMAL(15, 6),
  activation_energy DECIMAL(15, 6),
  description TEXT,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  FOREIGN KEY (from_compound_id) REFERENCES compounds(id),
  FOREIGN KEY (to_compound_id) REFERENCES compounds(id)
);
```

## 功能演示

### 1. 用户认证
- 用户可以注册新账户
- 登录后获得 JWT token
- Token 用于后续 API 请求认证

### 2. 化合物管理
- 创建、编辑、删除化合物
- 搜索和浏览化合物库
- 查看化合物的物理化学性质

### 3. 反应路径配置
- 定义化合物之间的反应关系
- 设置反应能量变化和激活能
- 管理复杂的反应网络

### 4. 图形化可视化
- 实时展示反应网络图
- 拖拽节点进行布局调整
- 缩放和平移视图

### 5. 路径算法计算
- BFS 算法找最短路径
- Dijkstra 算法找最低能量路径
- 算法结果高亮显示

## 常见问题

### Q: 如何重置数据库？
A: 删除 docker volume 并重新启动
```bash
docker-compose down -v
docker-compose up -d
```

### Q: 如何修改端口？
A: 修改 docker-compose.yml 中的端口配置

### Q: 如何添加新的用户？
A: 通过前端注册页面或直接在数据库插入用户记录

### Q: JWT token 过期了怎么办？
A: 重新登录获取新的 token

## 开发建议

1. **后端开发**
   - 遵循 RESTful API 设计原则
   - 使用事务确保数据一致性
   - 添加适当的异常处理和日志记录

2. **前端开发**
   - 使用 Composition API 编写 Vue 组件
   - 使用 Pinia 管理全局状态
   - 遵循响应式设计原则

3. **性能优化**
   - 数据库查询优化（添加索引）
   - 前端资源的懒加载
   - API 响应数据的分页处理

## 许可证

MIT License

## 作者

毕业设计项目

## 更新日志

### v1.0.0 (2024-05-14)
- 初始版本发布
- 完成所有基本功能
- 支持 Docker 容器化部署

## 联系方式

如有问题或建议，请提出 Issue 或联系项目维护者。
