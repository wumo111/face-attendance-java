-- 创建数据库
CREATE DATABASE IF NOT EXISTS face_attendance DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE face_attendance;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    role VARCHAR(20) NOT NULL COMMENT '角色：ADMIN/USER',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='系统用户表';

-- 插入默认管理员 (密码: 123456)
INSERT INTO sys_user (username, password, role) VALUES ('admin', 'e10adc3949ba59abbe56e057f20f883e', 'ADMIN');

-- 员工表
CREATE TABLE IF NOT EXISTS employee (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender TINYINT COMMENT '性别：1男 2女',
    id_card VARCHAR(20) COMMENT '身份证号',
    address VARCHAR(255) COMMENT '地址',
    phone VARCHAR(20) COMMENT '电话',
    photo_url VARCHAR(255) COMMENT '照片路径',
    feature TEXT COMMENT '人脸特征值',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='员工表';

-- 抓拍记录表
CREATE TABLE IF NOT EXISTS capture_record (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    employee_id INT COMMENT '员工ID，未识别则为NULL',
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '抓拍时间',
    image_url VARCHAR(255) COMMENT '抓拍图片路径',
    score FLOAT COMMENT '相似度得分'
) COMMENT='抓拍记录表';

-- 考勤记录表
CREATE TABLE IF NOT EXISTS attendance_record (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    employee_id INT NOT NULL COMMENT '员工ID',
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '打卡时间',
    status TINYINT DEFAULT 0 COMMENT '状态：0正常 1迟到 2早退',
    FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE
) COMMENT='考勤记录表';
