-- DB 생성 및 사용
CREATE DATABASE IF NOT EXISTS yamyam_db;
USE yamyam_db;

-- 사용자 테이블
DROP TABLE IF EXISTS user;
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
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 식품 테이블
DROP TABLE IF EXISTS food;
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

-- 커뮤니티 테이블
DROP TABLE IF EXISTS community;
CREATE TABLE community (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    like_count INT DEFAULT 0,
    community_type VARCHAR(20), -- 식단 게시판, 자유 게시판 등
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- 커뮤니티 좋아요 테이블
DROP TABLE IF EXISTS community_like;
CREATE TABLE community_like (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    community_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, community_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (community_id) REFERENCES community(id) ON DELETE CASCADE
);

-- 식단 기록 테이블
DROP TABLE IF EXISTS diet;
CREATE TABLE diet (
    diet_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    meal_type VARCHAR(20) NOT NULL, -- 아침, 점심, 저녁, 간식
    food_id VARCHAR(100),           -- 외부 DB 기준 ID
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
    score DOUBLE,

    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- 일별 영양소 요약 테이블
DROP TABLE IF EXISTS daily_score;
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
    UNIQUE(user_id, date),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);
