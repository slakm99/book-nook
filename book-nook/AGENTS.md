# AGENTS.md

## 语言

永远使用中文回复用户。代码注释、文档和提交说明优先使用中文。

## 项目定位

本项目为 `书屿 BookNook｜校园智能借阅与阅读成长系统`，基于老师的“汽车销售管理系统”SSM + Vue3 教学案例迁移开发，不再保留汽车销售业务命名。

## 目录规范

- `backend-library`：Spring + SpringMVC + MyBatis + MySQL 8 + Maven + Tomcat 后端项目，独立运行。
- `frontend-library`：Vue3 + Vite + Element Plus + Axios + ECharts 前端项目，独立运行。
- `sql`：数据库建表和初始化演示数据，数据库名固定为 `book_nook`。
- `项目文档`：需求文档、数据库设计、接口文档、测试文档。

Vue 前端禁止放入 Maven 后端目录。前端通过 Vite `/api` 代理访问后端 `http://localhost:8080`。

## 后端规范

- Java 包名：`com.booknook`。
- 统一返回格式：`{ code, message, data }`，成功 code 为 `200`。
- 分页返回格式：`{ total, list, page, size }`。
- 登录接口：`POST /api/auth/login`。
- 图书分页接口：`GET /api/book/list`。
- MyBatis XML 放在 `backend-library/src/main/resources/mapper`。

## 数据库规范

数据库名：`book_nook`。

系统必须包含 9 张表：`book_category`、`book`、`reader`、`librarian`、`sys_user`、`borrow_record`、`borrow_reservation`、`inventory_log`、`reading_goal`。

## UI 规范

视觉风格为“校园图书馆 + 阅读空间”：暖米白背景、墨绿色导航、琥珀色强调色、圆角卡片、轻阴影、纸张感。列表必须有分页、加载状态、空状态和错误提示。图书封面可用 CSS 渐变兜底，不依赖外部图片。
