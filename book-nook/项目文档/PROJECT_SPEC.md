# 书屿 BookNook｜校园智能借阅与阅读成长系统需求说明

## 1. 项目背景

本项目基于老师“汽车销售管理系统（SSM + Vue3）”教学案例迁移开发。原案例中的车辆、客户、试驾预约、订单、库存、统计模块迁移为图书、读者、借阅预约、借阅记录、馆藏日志、阅读统计模块。

## 2. 技术栈

- 后端：Spring + SpringMVC + MyBatis + MySQL 8 + Maven + Tomcat 9
- 前端：Vue3 + Vite + Element Plus + Axios + ECharts
- 版本管理：Git

## 3. 角色

- `ADMIN`：管理员，管理全部业务和统计。
- `LIBRARIAN`：馆员，管理图书、读者、借阅、归还、预约。
- `READER`：读者，浏览图书、查看借阅与预约、设置阅读目标、获取推荐。

## 4. 项目范围

1. 搭建 `book-nook` 项目结构。
2. 编写项目协作规范 `AGENTS.md`。
3. 编写需求文档和数据库设计文档。
4. 创建 `sql/book_nook.sql`，包含 9 张表、中文注释、主外键和初始化演示数据。
5. 配置 MyBatis Generator，面向 9 张表生成实体类、Mapper 和 XML。
6. 完成登录接口、登录页、主布局。
7. 完成图书分页接口 `GET /api/book/list`。
8. 完成图书列表页，支持关键词、作者、分类、库存筛选和分页展示。

## 5. 核心功能规划

### 基础功能

- 登录和角色权限控制
- 图书分类 CRUD
- 图书 CRUD、分页、搜索和筛选
- 读者 CRUD
- 借书、还书、借阅记录查询
- 图书预约和预约记录查询
- 馆藏变动日志
- 仪表盘和数据统计

### 亮点功能

- 阅读目标：读者设置月度或学期目标，归还图书后自动增加完成数量。
- 规则推荐：按读者历史借阅最多分类推荐库存充足且未借过的热门图书。
- 今日盲选一本书：随机返回一本当前可借图书。

## 6. 核心接口

### 登录

`POST /api/auth/login`

请求：

```json
{
  "username": "admin",
  "password": "123456"
}
```

响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "xxx",
    "username": "admin",
    "role": "ADMIN",
    "refId": null
  }
}
```

### 图书分页

`GET /api/book/list?page=1&size=10&keyword=Java&author=&categoryId=&stockStatus=available`

响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 20,
    "list": [],
    "page": 1,
    "size": 10
  }
}
```

## 7. 前端页面

已完成：

- 登录页：暖米白背景、墨绿色品牌区域、账号密码登录。
- 主布局：墨绿色侧边导航、顶部用户信息、阅读空间感背景。
- 图书馆藏页：图书封面卡片流 + 分页 + 筛选项。

## 8. 默认演示账号

- 管理员：`admin / 123456`
- 馆员：`librarian / 123456`
- 读者：`reader / 123456`
