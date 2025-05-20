-- 기존 테이블 삭제 (외래키 역순)
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS community;
DROP TABLE IF EXISTS diet;
DROP TABLE IF EXISTS user;

-- DB 생성 (이미 있으면 생략 가능)
CREATE DATABASE IF NOT EXISTS yamyam_db;
USE yamyam_db;

-- user 테이블 (user_id 추가 반영)
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100),
    gender VARCHAR(10),
    height INT,
    weight INT,
    age INT,
    special_note TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- diet 테이블
CREATE TABLE diet (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    meal_time ENUM('BREAKFAST', 'LUNCH', 'DINNER', 'SNACK'),
    food_name VARCHAR(255),
    calories INT,
    protein DECIMAL(5,2),
    fat DECIMAL(5,2),
    carbs DECIMAL(5,2),
    eaten_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- community 테이블
CREATE TABLE community (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    like_count INT DEFAULT 0, -- 좋아요 카운트
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- comment 테이블
CREATE TABLE comment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    community_id INT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (community_id) REFERENCES community(id)
);
CREATE TABLE community_like (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    community_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, community_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (community_id) REFERENCES community(id)
);
