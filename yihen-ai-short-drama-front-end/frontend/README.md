# frontend（前端）

## 技术栈
- Vue 3
- Vite
- Vue Router
- Pinia
- Axios
- Sass

## 本地开发

```bash
cd yihen-ai-short-drama-front-end/frontend
npm install
npm run dev
```

默认地址：`http://localhost:3000`

## API 与 WebSocket 代理

开发环境已在 `vite.config.js` 配置代理：
- `/api` -> `http://localhost:8080`
- `/webSocket` -> `ws://localhost:8080`

所以前端本地开发时不需要手动改后端端口。

## 构建

```bash
npm run build
```

## 目录说明

```text
src/
├─ api/          # 接口封装
├─ components/   # 组件
├─ composables/  # 组合式逻辑
├─ router/       # 路由
├─ stores/       # 状态管理
└─ views/        # 页面
```

