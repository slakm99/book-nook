DROP DATABASE IF EXISTS book_nook;
CREATE DATABASE book_nook DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE book_nook;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS reading_goal;
DROP TABLE IF EXISTS inventory_log;
DROP TABLE IF EXISTS borrow_reservation;
DROP TABLE IF EXISTS borrow_record;
DROP TABLE IF EXISTS sys_user;
DROP TABLE IF EXISTS librarian;
DROP TABLE IF EXISTS reader;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS book_category;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE book_category (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
  category_name VARCHAR(50) NOT NULL COMMENT '分类名称',
  description VARCHAR(200) COMMENT '分类说明',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0停用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='图书分类';

CREATE TABLE book (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '图书ID',
  isbn VARCHAR(32) NOT NULL COMMENT 'ISBN，唯一',
  title VARCHAR(120) NOT NULL COMMENT '书名',
  author VARCHAR(80) NOT NULL COMMENT '作者',
  publisher VARCHAR(100) COMMENT '出版社',
  category_id BIGINT NOT NULL COMMENT '分类ID',
  description TEXT COMMENT '图书简介',
  cover_url VARCHAR(255) COMMENT '封面地址，可为空',
  tags VARCHAR(120) COMMENT '标签，逗号分隔',
  stock_total INT NOT NULL DEFAULT 0 COMMENT '馆藏总量',
  stock_available INT NOT NULL DEFAULT 0 COMMENT '可借库存',
  borrow_count INT NOT NULL DEFAULT 0 COMMENT '累计借阅次数',
  status VARCHAR(20) NOT NULL DEFAULT 'ON_SHELF' COMMENT '状态：ON_SHELF上架 OFF_SHELF下架',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY uk_book_isbn (isbn),
  KEY idx_book_category (category_id),
  KEY idx_book_title (title),
  CONSTRAINT fk_book_category FOREIGN KEY (category_id) REFERENCES book_category(id),
  CONSTRAINT chk_book_stock CHECK (stock_total >= 0 AND stock_available >= 0 AND stock_available <= stock_total)
) COMMENT='图书馆藏';

CREATE TABLE reader (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '读者ID',
  student_no VARCHAR(30) NOT NULL COMMENT '学号，唯一',
  name VARCHAR(40) NOT NULL COMMENT '读者姓名',
  phone VARCHAR(20) COMMENT '手机号',
  college VARCHAR(80) COMMENT '学院',
  grade VARCHAR(20) COMMENT '年级',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1正常 0停用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY uk_reader_student_no (student_no)
) COMMENT='读者信息';

CREATE TABLE librarian (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '馆员ID',
  staff_no VARCHAR(30) NOT NULL COMMENT '工号，唯一',
  name VARCHAR(40) NOT NULL COMMENT '馆员姓名',
  phone VARCHAR(20) COMMENT '手机号',
  position VARCHAR(50) COMMENT '岗位',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1在岗 0停用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY uk_librarian_staff_no (staff_no)
) COMMENT='馆员信息';

CREATE TABLE sys_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  username VARCHAR(50) NOT NULL COMMENT '登录用户名',
  password VARCHAR(100) NOT NULL COMMENT '登录密码，演示环境明文存储',
  role VARCHAR(20) NOT NULL COMMENT '角色：ADMIN/LIBRARIAN/READER',
  ref_id BIGINT COMMENT '关联业务ID：reader.id或librarian.id，ADMIN可为空',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0停用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY uk_sys_user_username (username),
  KEY idx_sys_user_role (role)
) COMMENT='登录用户和角色';

CREATE TABLE borrow_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '借阅记录ID',
  reader_id BIGINT NOT NULL COMMENT '读者ID',
  book_id BIGINT NOT NULL COMMENT '图书ID',
  borrow_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借出时间',
  due_time DATETIME NOT NULL COMMENT '应还时间',
  return_time DATETIME COMMENT '归还时间',
  status VARCHAR(20) NOT NULL DEFAULT 'BORROWED' COMMENT '状态：BORROWED借阅中 RETURNED已归还 OVERDUE逾期',
  renew_count INT NOT NULL DEFAULT 0 COMMENT '续借次数',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  KEY idx_borrow_reader (reader_id),
  KEY idx_borrow_book (book_id),
  KEY idx_borrow_status_due (status, due_time),
  CONSTRAINT fk_borrow_reader FOREIGN KEY (reader_id) REFERENCES reader(id),
  CONSTRAINT fk_borrow_book FOREIGN KEY (book_id) REFERENCES book(id)
) COMMENT='借阅记录';

CREATE TABLE borrow_reservation (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '预约ID',
  reader_id BIGINT NOT NULL COMMENT '读者ID',
  book_id BIGINT NOT NULL COMMENT '图书ID',
  queue_no INT NOT NULL COMMENT '排队序号',
  status VARCHAR(20) NOT NULL DEFAULT 'WAITING' COMMENT '状态：WAITING等待 READY可取书 CANCELLED已取消 FINISHED已完成 EXPIRED已过期',
  pickup_deadline DATETIME COMMENT '取书截止时间',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  KEY idx_reservation_reader (reader_id),
  KEY idx_reservation_book_status (book_id, status),
  CONSTRAINT fk_reservation_reader FOREIGN KEY (reader_id) REFERENCES reader(id),
  CONSTRAINT fk_reservation_book FOREIGN KEY (book_id) REFERENCES book(id)
) COMMENT='图书预约';

CREATE TABLE inventory_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '库存日志ID',
  book_id BIGINT NOT NULL COMMENT '图书ID',
  change_type VARCHAR(30) NOT NULL COMMENT '变动类型：INIT/BORROW/RETURN/ADJUST',
  before_stock INT NOT NULL COMMENT '变动前可借库存',
  change_quantity INT NOT NULL COMMENT '变动数量',
  after_stock INT NOT NULL COMMENT '变动后可借库存',
  remark VARCHAR(200) COMMENT '备注',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  KEY idx_inventory_book (book_id),
  CONSTRAINT fk_inventory_book FOREIGN KEY (book_id) REFERENCES book(id)
) COMMENT='馆藏变动日志';

CREATE TABLE reading_goal (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '阅读目标ID',
  reader_id BIGINT NOT NULL COMMENT '读者ID',
  target_period VARCHAR(30) NOT NULL COMMENT '目标周期：2026-06或2026春季学期',
  target_count INT NOT NULL DEFAULT 0 COMMENT '目标读完数量',
  completed_count INT NOT NULL DEFAULT 0 COMMENT '已完成数量',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY uk_goal_reader_period (reader_id, target_period),
  CONSTRAINT fk_goal_reader FOREIGN KEY (reader_id) REFERENCES reader(id)
) COMMENT='阅读目标';

INSERT INTO book_category(category_name, description, sort_order) VALUES
('文学小说','校园阅读与经典文学',1),
('计算机技术','编程、算法、软件工程',2),
('经济管理','管理学、商业与财务',3),
('心理成长','心理学、沟通与成长',4),
('历史人文','历史、哲学与通识',5),
('艺术设计','设计、美学与创意表达',6);

INSERT INTO book(isbn,title,author,publisher,category_id,description,cover_url,tags,stock_total,stock_available,borrow_count,status) VALUES
('9787020002207','围城','钱锺书','人民文学出版社',1,'以幽默讽刺的笔触写知识分子的婚恋与人生困局，语言机智耐读，适合作为现代文学阅读入门。','','经典,文学',8,6,23,'ON_SHELF'),
('9787536692930','三体','刘慈欣','重庆出版社',1,'从文明危机到宇宙尺度的科幻想象，情节紧凑、设定宏大，是校园科幻阅读和讨论度很高的热门馆藏。','','科幻,想象力',10,7,36,'ON_SHELF'),
('9787115428028','Java核心技术 卷I','Cay S. Horstmann','机械工业出版社',2,'系统讲解 Java 基础语法、面向对象、集合与图形界面等内容，适合课程学习和项目开发查阅。','','Java,编程',6,4,18,'ON_SHELF'),
('9787115546081','深入理解计算机系统','Randal E. Bryant','机械工业出版社',2,'从硬件、编译、内存到并发理解程序运行机制，适合计算机专业学生建立扎实的系统视角。','','CSAPP,系统',5,3,21,'ON_SHELF'),
('9787111612728','算法图解','Aditya Bhargava','人民邮电出版社',2,'用大量图示解释搜索、排序、递归、动态规划等常见算法，适合算法入门和考前快速复习。','','算法,入门',7,5,16,'ON_SHELF'),
('9787111636663','Python编程：从入门到实践','Eric Matthes','人民邮电出版社',2,'以项目实践带动 Python 学习，覆盖基础语法、数据处理与小型应用开发，适合零基础读者。','','Python,实践',9,8,13,'ON_SHELF'),
('9787302423287','数据库系统概论','王珊','高等教育出版社',2,'数据库课程经典教材，覆盖关系模型、SQL、规范化和数据库设计，是实训项目建表设计的重要参考。','','数据库,SQL',6,5,11,'ON_SHELF'),
('9787111213826','重构：改善既有代码的设计','Martin Fowler','人民邮电出版社',2,'通过具体案例讲解如何改善代码结构、降低维护成本，适合完成项目后进行代码优化和答辩说明。','','软件工程,重构',4,3,12,'ON_SHELF'),
('9787508649719','原则','Ray Dalio','中信出版社',3,'总结作者在生活、工作与组织管理中的决策方法，适合阅读成长、目标管理和团队协作主题展示。','','管理,成长',5,4,9,'ON_SHELF'),
('9787111471615','从0到1','Peter Thiel','中信出版社',3,'讨论创新创业中的差异化竞争和从无到有的思考方式，适合商业管理类读者拓展视野。','','创业,商业',5,5,7,'ON_SHELF'),
('9787300203430','管理学','Stephen P. Robbins','中国人民大学出版社',3,'管理学基础教材，覆盖计划、组织、领导和控制等核心内容，适合经管课程学习与案例分析。','','管理,教材',6,6,5,'ON_SHELF'),
('9787544291170','被讨厌的勇气','岸见一郎','机械工业出版社',4,'以对话形式介绍阿德勒心理学，讨论自我接纳、人际关系和生活勇气，适合心理成长阅读。','','心理,成长',8,6,20,'ON_SHELF'),
('9787213087892','蛤蟆先生去看心理医生','Robert de Board','浙江人民出版社',4,'借童话角色讲述心理咨询过程，帮助读者理解情绪、原生家庭和自我修复，阅读门槛友好。','','心理,疗愈',7,6,14,'ON_SHELF'),
('9787508694672','非暴力沟通','Marshall Rosenberg','华夏出版社',4,'围绕观察、感受、需要和请求建立沟通方法，适合宿舍、社团和团队协作中的沟通训练。','','沟通,心理',6,4,15,'ON_SHELF'),
('9787101080759','史记','司马迁','中华书局',5,'中国纪传体通史经典，通过人物传记呈现历史兴衰和人性选择，适合传统文化与历史阅读。','','历史,经典',4,3,8,'ON_SHELF'),
('9787101052039','万历十五年','黄仁宇','中华书局',5,'以万历十五年为切口观察明代政治、财政与制度运行，用细节呈现大历史的结构性问题。','','历史,明史',6,5,19,'ON_SHELF'),
('9787108066824','人类简史','Yuval Noah Harari','中信出版社',5,'用宏观视角梳理人类从认知革命到现代社会的发展，适合通识阅读和跨学科讨论。','','历史,通识',7,5,22,'ON_SHELF'),
('9787558609251','设计中的设计','原研哉','广西师范大学出版社',6,'从日常物品和生活经验谈设计观念，文字清澈克制，适合培养审美意识与产品设计思维。','','设计,美学',5,4,10,'ON_SHELF'),
('9787115422330','写给大家看的设计书','Robin Williams','人民邮电出版社',6,'用亲切案例讲清对比、重复、对齐、亲密性四大原则，适合前端页面和海报排版入门。','','设计,排版',6,6,6,'ON_SHELF'),
('9787535683489','艺术的故事','E.H. Gombrich','广西美术出版社',6,'以通俗叙事介绍西方艺术发展脉络，兼具知识性和可读性，是艺术史入门的经典读物。','','艺术史,审美',4,2,13,'ON_SHELF');

UPDATE book SET cover_url = CONCAT('https://covers.openlibrary.org/b/isbn/', isbn, '-L.jpg');

INSERT INTO reader(student_no,name,phone,college,grade,status) VALUES
('202401001','林小舟','13800010001','信息工程学院','2024级',1),
('202401002','陈南星','13800010002','文学院','2024级',1),
('202301003','周予安','13800010003','管理学院','2023级',1),
('202201004','许知夏','13800010004','艺术学院','2022级',1),
('202101005','顾明远','13800010005','历史学院','2021级',1);

INSERT INTO librarian(staff_no,name,phone,position,status) VALUES
('L2026001','沈馆员','13900020001','流通服务',1),
('L2026002','赵馆员','13900020002','馆藏维护',1);

INSERT INTO sys_user(username,password,role,ref_id,status) VALUES
('admin','123456','ADMIN',NULL,1),
('librarian','123456','LIBRARIAN',1,1),
('linxiaozhou','123456','READER',1,1),
('chennanxing','123456','READER',2,1),
('zhouyuan','123456','READER',3,1),
('xuzhixia','123456','READER',4,1),
('gumingyuan','123456','READER',5,1);

INSERT INTO borrow_record(reader_id,book_id,borrow_time,due_time,return_time,status,renew_count) VALUES
(1,3,DATE_SUB(NOW(), INTERVAL 8 DAY),DATE_ADD(NOW(), INTERVAL 22 DAY),NULL,'BORROWED',0),
(1,12,DATE_SUB(NOW(), INTERVAL 30 DAY),DATE_SUB(NOW(), INTERVAL 1 DAY),DATE_SUB(NOW(), INTERVAL 3 DAY),'RETURNED',0),
(2,2,DATE_SUB(NOW(), INTERVAL 5 DAY),DATE_ADD(NOW(), INTERVAL 25 DAY),NULL,'BORROWED',0),
(3,16,DATE_SUB(NOW(), INTERVAL 40 DAY),DATE_SUB(NOW(), INTERVAL 10 DAY),NULL,'OVERDUE',1),
(4,18,DATE_SUB(NOW(), INTERVAL 20 DAY),DATE_ADD(NOW(), INTERVAL 10 DAY),NULL,'BORROWED',0),
(5,17,DATE_SUB(NOW(), INTERVAL 60 DAY),DATE_SUB(NOW(), INTERVAL 30 DAY),DATE_SUB(NOW(), INTERVAL 32 DAY),'RETURNED',0);

INSERT INTO borrow_reservation(reader_id,book_id,queue_no,status,pickup_deadline,create_time) VALUES
(2,3,1,'WAITING',NULL,DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3,2,1,'WAITING',NULL,DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1,20,1,'READY',DATE_ADD(NOW(), INTERVAL 2 DAY),DATE_SUB(NOW(), INTERVAL 3 DAY));

INSERT INTO inventory_log(book_id,change_type,before_stock,change_quantity,after_stock,remark) VALUES
(3,'BORROW',5,-1,4,'读者借出 Java核心技术'),
(12,'RETURN',5,1,6,'读者归还 被讨厌的勇气'),
(20,'BORROW',3,-1,2,'艺术的故事借出'),
(1,'INIT',0,8,8,'初始化馆藏');

INSERT INTO reading_goal(reader_id,target_period,target_count,completed_count) VALUES
(1,DATE_FORMAT(CURDATE(),'%Y-%m'),4,1),
(2,DATE_FORMAT(CURDATE(),'%Y-%m'),3,0),
(3,'2026春季学期',8,3),
(4,DATE_FORMAT(CURDATE(),'%Y-%m'),2,0);
