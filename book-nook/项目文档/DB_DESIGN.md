# 数据库设计说明

数据库名：`book_nook`

## 表清单

| 表名 | 中文名 | 说明 |
| --- | --- | --- |
| `book_category` | 图书分类 | 管理文学、技术、心理等分类 |
| `book` | 图书馆藏 | 保存 ISBN、书名、作者、库存、标签 |
| `reader` | 读者信息 | 保存学生读者资料 |
| `librarian` | 馆员信息 | 保存馆员资料 |
| `sys_user` | 登录用户 | 保存用户名、密码、角色和业务关联 ID |
| `borrow_record` | 借阅记录 | 保存借书、还书、逾期状态 |
| `borrow_reservation` | 图书预约 | 保存排队预约和可取书状态 |
| `inventory_log` | 馆藏日志 | 保存库存变动流水 |
| `reading_goal` | 阅读目标 | 保存读者月度或学期阅读目标 |

## 核心关系

- `book.category_id` -> `book_category.id`
- `borrow_record.reader_id` -> `reader.id`
- `borrow_record.book_id` -> `book.id`
- `borrow_reservation.reader_id` -> `reader.id`
- `borrow_reservation.book_id` -> `book.id`
- `inventory_log.book_id` -> `book.id`
- `reading_goal.reader_id` -> `reader.id`

## 关键约束

1. `book.isbn` 唯一。
2. `reader.student_no` 唯一。
3. `librarian.staff_no` 唯一。
4. `sys_user.username` 唯一。
5. `book.stock_available` 不能小于 0，且不能大于 `stock_total`。
6. `reading_goal` 同一读者同一目标周期唯一。

## 初始化演示数据

`sql/book_nook.sql` 内置：

- 6 个图书分类
- 20 本图书
- 5 个读者
- 2 个馆员
- 管理员、馆员、读者默认账号
- 借阅记录、预约记录、库存日志、阅读目标示例数据

## MyBatis Generator

配置文件：

`backend-library/src/main/resources/generatorConfig.xml`

生成命令：

```bash
cd backend-library
mvn mybatis-generator:generate
```

生成目标：

- 实体类：`backend-library/src/main/java/com/booknook/entity`
- Mapper 接口：`backend-library/src/main/java/com/booknook/mapper`
- Mapper XML：`backend-library/src/main/resources/mapper`
