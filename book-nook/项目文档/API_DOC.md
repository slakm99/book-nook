# 接口文档

## 通用返回格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

## 登录

### POST `/api/auth/login`

请求体：

```json
{
  "username": "admin",
  "password": "123456"
}
```

成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "Bearer token",
    "username": "admin",
    "role": "ADMIN",
    "refId": null
  }
}
```

## 图书分页

### GET `/api/book/list`

参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `page` | number | 否 | 当前页，默认 1 |
| `size` | number | 否 | 每页数量，默认 10 |
| `keyword` | string | 否 | 按书名、ISBN、标签搜索 |
| `author` | string | 否 | 作者筛选 |
| `categoryId` | number | 否 | 分类筛选 |
| `stockStatus` | string | 否 | `available` 有库存，`empty` 无库存 |

示例：

`GET http://localhost:8080/api/book/list?page=1&size=8&keyword=Java`

## 借阅与预约

### POST `/api/borrow`

管理员或馆员办理直接借书。库存必须大于 0，成功后会同时新增借阅记录、扣减可借库存、写入馆藏日志。

```json
{
  "readerId": 1,
  "bookId": 1
}
```

### PUT `/api/borrow/{id}/return`

管理员或馆员办理归还。归还后会恢复库存、写入馆藏日志、更新阅读目标完成数；如果该书存在等待预约，会自动把最早一位预约者改为“可取书”。

### POST `/api/reservation`

创建图书预约。读者登录时只能给自己预约；管理员和馆员可以代读者预约。

业务规则：

- 如果图书当前有可借库存，预约状态直接变为 `READY`，表示“可取书”，取书截止时间为 2 天后。
- 如果图书当前无可借库存，预约状态为 `WAITING`，表示“等待排队”。
- 同一读者不能重复预约同一本仍有效的图书。

```json
{
  "readerId": 1,
  "bookId": 1
}
```

### PUT `/api/reservation/{id}/pickup`

管理员或馆员办理“预约取书”。只有 `READY` 状态可以办理。成功后系统会：

1. 新增 `borrow_record` 借阅记录；
2. 扣减 `book.stock_available`；
3. 把预约状态改成 `FINISHED`；
4. 写入 `inventory_log` 馆藏日志。

### PUT `/api/reservation/{id}/cancel`

取消预约。读者只能取消自己的 `WAITING` 或 `READY` 预约；管理员和馆员可以取消有效预约。

### GET `/api/reservation/list`

查询预约记录。读者端只返回自己的预约；管理员和馆员端返回全部预约。

常见状态：

| 状态 | 说明 |
| --- | --- |
| `WAITING` | 等待排队 |
| `READY` | 可取书 |
| `FINISHED` | 已办理取书并生成借阅 |
| `CANCELLED` | 已取消 |
| `EXPIRED` | 超过取书截止时间 |
