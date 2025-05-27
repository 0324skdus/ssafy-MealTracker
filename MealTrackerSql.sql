-- DB 생성
CREATE DATABASE IF NOT EXISTS yamyam_db;
USE yamyam_db;

-- 기존 테이블 제거 (순서 주의: FK 순환 방지)
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS community_like;
DROP TABLE IF EXISTS community;
DROP TABLE IF EXISTS daily_score;
DROP TABLE IF EXISTS diet;
DROP TABLE IF EXISTS food;
DROP TABLE IF EXISTS user;

-- 유저 테이블
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    gender CHAR(1) CHECK (gender IN ('M', 'F')),
    height DOUBLE,
    weight DOUBLE,
    age INT,
    special_note ENUM('체중감량', '체중유지', '근육량 증가') DEFAULT '체중유지',
    activity_level ENUM('L', 'M', 'H') DEFAULT 'M',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    custom_bmr DOUBLE
);

-- 커뮤니티 테이블
CREATE TABLE community (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    like_count INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    community_type VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- 커뮤니티 좋아요 테이블
CREATE TABLE community_like (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    community_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, community_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (community_id) REFERENCES community(id) ON DELETE CASCADE
);

-- 댓글 테이블
CREATE TABLE comment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    community_id INT NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    secret TINYINT(1) DEFAULT 0,
    FOREIGN KEY (community_id) REFERENCES community(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- 식단 테이블
CREATE TABLE diet (
    diet_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    meal_type VARCHAR(20) NOT NULL,
    food_id VARCHAR(100),
    food_name VARCHAR(100),
    kcal DOUBLE,
    carbo DOUBLE,
    protein DOUBLE,
    fat DOUBLE,
    saturatedfat DOUBLE,
    sugar DOUBLE,
    fiber DOUBLE,
    transfat DOUBLE,
    date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- 음식 테이블
CREATE TABLE food (
    food_id INT AUTO_INCREMENT PRIMARY KEY,
    food_name VARCHAR(100) NOT NULL,
    kcal DOUBLE,
    carbo DOUBLE,
    protein DOUBLE,
    fat DOUBLE,
    saturatedfat DOUBLE,
    sugar DOUBLE,
    fiber DOUBLE,
    transfat DOUBLE
);

-- 일일 점수 테이블
CREATE TABLE daily_score (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    date DATE NOT NULL,
    total_kcal DOUBLE,
    total_carbo DOUBLE,
    total_protein DOUBLE,
    total_fat DOUBLE,
    total_saturatedfat DOUBLE,
    total_sugar DOUBLE,
    total_fiber DOUBLE,
    total_transfat DOUBLE,
    score DOUBLE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (user_id, date),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- 알림 테이블
CREATE TABLE notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receiver_id VARCHAR(100) NOT NULL,
    sender_id VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    community_id INT,
    is_read TINYINT(1) DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
