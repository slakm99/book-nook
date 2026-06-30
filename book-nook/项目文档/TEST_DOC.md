# 测试文档

## 环境准备

1. MySQL 8 已启动，root 密码为 `123456`。
2. Tomcat 9 已配置到 IDEA。
3. Node.js 和 npm 可用。

## 数据库初始化

```bash
mysql -u root -p123456 < sql/book_nook.sql
```

## 后端验证

```bash
cd backend-library
mvn clean package -DskipTests
```

接口验证：

- `POST http://localhost:8080/api/auth/login`
- `GET http://localhost:8080/api/book/list?page=1&size=10`

## 前端验证

```bash
cd frontend-library
npm install
npm run dev
```

访问：

`http://localhost:5173/login`

默认账号：

- `admin / 123456`
- `librarian / 123456`
- `reader / 123456`

## Day03 对应自检：图书和分类完整 CRUD

### 后端接口测试表

| 方法 | 地址 | 请求体/参数 | 预期结果 |
|---|---|---|---|
| GET | `/api/book/list?page=1&size=8` | 可带 `keyword`、`author`、`categoryId`、`stockStatus` | 返回图书分页数据 |
| POST | `/api/book` | `isbn`、`title`、`author`、`categoryId`、`stockTotal`、`stockAvailable` 等 | 新增成功；ISBN 重复时返回错误 |
| PUT | `/api/book/{id}` | 图书修改数据 | 修改成功；库存不合法时返回错误 |
| DELETE | `/api/book/{id}` | 无 | 删除成功；已有借阅记录时不允许删除 |
| GET | `/api/category/list` | 无 | 返回所有启用分类，前端下拉框动态加载 |
| POST | `/api/category` | `categoryName`、`description`、`sortOrder` | 新增成功；分类名重复时返回错误 |
| PUT | `/api/category/{id}` | 分类修改数据 | 修改成功；分类名重复时返回错误 |
| DELETE | `/api/category/{id}` | 无 | 删除成功；分类下有图书时不允许删除 |
| POST | `/api/reader` | `studentNo`、`name`、`phone` 等 | 新增读者；学号重复或手机号格式错误时返回错误 |

### 前端流程测试

1. 进入“图书馆藏/图书探索”页面，分类下拉框从后端 `/api/category/list` 动态加载，不是写死在页面里。
2. 选择分类、关键词、作者或库存状态，点击搜索，列表刷新。
3. 管理员/馆员点击“新增图书”，弹出表单，填写必填项后保存，列表刷新。
4. 点击“编辑”，弹窗自动回显原图书数据，修改后保存，列表刷新。
5. 点击“删除”，出现确认弹窗，确认后删除并刷新列表。
6. 读者登录后图书页只显示“预约”，不会显示新增、编辑、删除按钮。

### 异常场景测试

- 新增图书时 ISBN、书名、作者、分类为空：前端表单提示必填。
- 新增图书时 ISBN 已存在：后端返回“ISBN不能重复”。
- 可借库存大于馆藏总量：前端和后端都会拦截。
- 新增分类时分类名称为空：前端表单提示必填。
- 新增分类时分类名称重复：后端返回“分类名称不能重复”。
- 新增读者时学号或姓名为空：前端表单提示必填。
- 新增读者时学号重复：后端返回“学号不能重复”。
- 手机号格式错误：前端和后端都会拦截。
- 读者手动请求后台管理接口：后端返回 403，提示无权进行后台管理操作。

### Day03 代码结构自检

- `BookService` 已拆分为接口，`BookServiceImpl` 为实现类。
- `LibraryService` 已拆分为接口，`LibraryServiceImpl` 为实现类。
- Controller 只负责接收请求、调用 Service、返回统一 `Result`，业务规则写在 Service 层。
- 图书分类下拉框由接口动态加载。
- 图书、分类、读者页面均具备新增、编辑、删除弹窗和保存后刷新列表能力。
