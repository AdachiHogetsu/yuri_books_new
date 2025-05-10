-- 禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;
#mysql -u Yuri_books -p先登录
#source database_reset.sql运行此脚本
-- 选择数据库
USE yuri_books;

-- 删除现有表(按依赖关系逆序)
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS recommend;
DROP TABLE IF EXISTS goods;
DROP TABLE IF EXISTS type;
DROP TABLE IF EXISTS user;

-- 创建用户表
CREATE TABLE user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50),
    phone VARCHAR(20),
    address VARCHAR(200),
    isadmin TINYINT(1) DEFAULT 0,
    isvalidate TINYINT(1) DEFAULT 0,
    issellman TINYINT(1) DEFAULT 0,
    selltypeid INT
);

-- 创建商品类型表
CREATE TABLE type (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    encodeName VARCHAR(100)
);

-- 创建商品表
CREATE TABLE goods (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    cover VARCHAR(200),
    image1 VARCHAR(200),
    image2 VARCHAR(200),
    price FLOAT NOT NULL,
    intro TEXT,
    stock INT DEFAULT 0,
    type_id INT,
    isScroll TINYINT(1) DEFAULT 0,
    isHot TINYINT(1) DEFAULT 0,
    isNew TINYINT(1) DEFAULT 0,
    FOREIGN KEY (type_id) REFERENCES type(id)
);

-- 创建推荐商品表
CREATE TABLE recommend (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type INT NOT NULL COMMENT '1条幅 2热销 3新品',
    goods_id INT NOT NULL,
    type_id INT,
    FOREIGN KEY (goods_id) REFERENCES goods(id)
);

-- 创建订单表
CREATE TABLE `orders` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    total FLOAT NOT NULL,
    amount INT NOT NULL,
    status INT DEFAULT 1 COMMENT '1未付款/2已付款/3已发货/4已完成',
    paytype INT COMMENT '1微信/2支付宝/3货到付款',
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL,
    datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 创建订单项表
CREATE TABLE order_item (
    id INT PRIMARY KEY AUTO_INCREMENT,
    price FLOAT NOT NULL,
    amount INT NOT NULL,
    goods_name VARCHAR(100),
    goods_id INT NOT NULL,
    order_id INT NOT NULL,
    FOREIGN KEY (goods_id) REFERENCES goods(id),
    FOREIGN KEY (order_id) REFERENCES `order`(id)
);

-- 启用外键检查
SET FOREIGN_KEY_CHECKS = 1;
-- 添加初始用户数据
-- 管理员用户(admin/123)
INSERT INTO user (username, email, password, name, phone, address, isadmin, isvalidate, issellman) 
VALUES ('admin', 'admin@yuri_books.com', '123', 'admin', '13431709400', 'heaven', 1, 1, 0);