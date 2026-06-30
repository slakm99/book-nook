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
