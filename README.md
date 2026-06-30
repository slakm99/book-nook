# 书屿 BookNook｜校园智能借阅与阅读成长系统

本仓库是课程实训项目“书屿 BookNook｜校园智能借阅与阅读成长系统”，基于老师的 SSM + Vue3 教学案例迁移开发为图书管理主题。

## 项目结构

- `book-nook/backend-library`：SSM 后端项目，Maven 构建，Tomcat 运行。
- `book-nook/frontend-library`：Vue3 + Vite + Element Plus 前端项目。
- `book-nook/sql/book_nook.sql`：完整数据库脚本，包含建库、9 张表、主外键、字段注释和测试数据。
- `book-nook/项目文档`：需求文档、数据库设计、接口文档、测试文档和代码功能说明。
- `项目演示资料`：答辩演示辅助资料。

## 技术栈

- 后端：Spring、SpringMVC、MyBatis、MySQL 8、Maven、Tomcat
- 前端：Vue3、Vite、Element Plus、Axios、ECharts
- 数据库：MySQL 8
- 版本管理：Git / GitHub

## 主要功能

- 登录与角色菜单：管理员、馆员、读者
- 图书分类管理
- 图书馆藏管理：分页、搜索、分类筛选、库存筛选、新增、编辑、删除
- 读者管理
- 借书、还书、借阅记录
- 图书预约与预约记录
- 馆藏变动日志
- 仪表盘与统计图表
- 阅读目标、猜你喜欢、今日盲选一本书

## 数据库说明

数据库名：`book_nook`

核心 9 张表：

1. `book_category`
2. `book`
3. `reader`
4. `librarian`
5. `sys_user`
6. `borrow_record`
7. `borrow_reservation`
8. `inventory_log`
9. `reading_goal`

导入脚本：

```sql
source book-nook/sql/book_nook.sql;
```

后端目录中也保留了一份完整脚本：

```text
book-nook/backend-library/sql/book_nook.sql
```

## 运行方式

### 后端

用 IDEA 打开 `book-nook/backend-library`，配置 Tomcat 后运行。数据库连接配置在：

```text
book-nook/backend-library/src/main/resources/application.properties
```

也可以先打包：

```bash
cd book-nook/backend-library
mvn package
```

### 前端

```bash
cd book-nook/frontend-library
npm install
npm run dev
```

前端默认地址：

```text
http://127.0.0.1:5173/login
```

前端通过 Vite `/api` 代理访问后端 `8080` 端口。

## 默认账号

- 管理员：`admin / 123456`
- 馆员：`librarian / 123456`
- 读者：`reader / 123456`
