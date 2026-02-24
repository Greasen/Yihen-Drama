# Yihen-Drama（AI 短剧生成平台）

一个覆盖「文本输入 → 信息提取 → 人物/场景图生成 → 分镜生成 → 视频生成/编辑」的全流程短剧创作系统。

仓库包含：
- 后端：`yihen-drama`（Spring Boot）
- 前端：`yihen-ai-short-drama-front-end/frontend`（Vue 3 + Vite）
- 容器编排：支持**开发模式**与**一键全容器模式**

---

## 1. 功能概览

- 项目/章节管理：创建、编辑、删除、搜索、分页
- 信息提取：人物与场景提取
- 资产管理：角色/场景增删改查、上传、搜索
- 分镜管理：生成、编辑、删除、关联角色/场景、提示词
- 视频流程：首帧、分镜视频提示词、视频任务提交与状态回传（WebSocket）
- 模型管理：厂商、模型实例、默认模型实例（文本/图像/视频/语音）
- ES 搜索与补全：项目、角色、场景

---

## 2. 运行模式（重点）

### A. 本地开发模式（推荐日常开发）

前后端本机运行；MySQL/Redis/RabbitMQ/MinIO/ES/Kibana 用容器。

#### 1) 启动中间件
```powershell
.\infra-up.ps1
```
或
```bat
infra-up.bat
```
等价命令：
```bash
docker compose -f docker-compose.infra.yml up -d --build
```

#### 2) 启动后端（本机）
```bash
cd yihen-drama
mvn spring-boot:run
```
默认端口：`8080`

#### 3) 启动前端（本机）
```bash
cd yihen-ai-short-drama-front-end/frontend
npm install
npm run dev
```
默认端口：`3000`

说明：
- 前端开发代理已配置：`/api` 与 `/webSocket` 自动转发到 `localhost:8080`
- 不需要在前端手工改成 `8080`

---

### B. 一键全容器模式（部署/演示）

```powershell
.\deploy.ps1
```
或
```bat
deploy.bat
```
等价命令：
```bash
docker compose -f docker-compose.full.yml up -d --build
```

---

## 3. 访问地址

- 前端：`http://localhost:3000`
- 后端：`http://localhost:8080`
- API 文档：`http://localhost:8080/doc.html`
- MinIO Console：`http://localhost:9001`
- RabbitMQ Console：`http://localhost:15672`
- Elasticsearch：`http://localhost:9200`
- Kibana：`http://localhost:5601`

---

## 4. 数据库初始化

- 初始化脚本：`yihen-drama/sql/init_schema.sql`
- 在 MySQL 数据卷为空时自动执行
- 如需重置初始化：
```bash
docker compose -f docker-compose.full.yml down -v
docker compose -f docker-compose.full.yml up -d --build
```

---

## 5. Elasticsearch 插件（IK + pinyin）

已内置在 `yihen-drama/docker/elasticsearch/Dockerfile`，随编排自动安装。

可验证：
```bash
docker compose -f docker-compose.full.yml exec es elasticsearch-plugin list
```

---

## 6. 关键配置说明

后端配置文件：`yihen-drama/src/main/resources/application.yml`

已改为环境变量优先，兼容两种运行模式。核心变量：
- `SPRING_DATASOURCE_URL/USERNAME/PASSWORD`
- `SPRING_DATA_REDIS_HOST/PORT/PASSWORD`
- `SPRING_RABBITMQ_HOST/PORT/USERNAME/PASSWORD`
- `SPRING_ELASTICSEARCH_URIS`
- `MINIO_END_POINT/MINIO_ACCESS_KEY/MINIO_SECRET_KEY`

---

## 7. 项目结构

```text
.
├─ docker-compose.infra.yml     # 仅中间件
├─ docker-compose.full.yml      # 前后端 + 中间件
├─ infra-up.ps1 / infra-up.bat
├─ deploy.ps1 / deploy.bat
├─ yihen-drama                  # 后端
└─ yihen-ai-short-drama-front-end/frontend  # 前端
```

---

## 8. 常见问题

### Q1: 后端报 Redis 连接失败
- 检查 `docker compose -f docker-compose.infra.yml ps`
- 确保 `redis` 已启动并健康

### Q2: 前端请求打到了 3000 而不是 8080
- 本地开发这是正常行为（Vite 代理）
- 真正后端仍是 `8080`

### Q3: DockerHub 拉取超时
- 可替换镜像源，或提前 `docker pull` 所需镜像

---

## 9. 子模块文档

- 后端文档：`yihen-drama/README.md`
- 前端文档：`yihen-ai-short-drama-front-end/frontend/README.md`

