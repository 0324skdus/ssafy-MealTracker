-- 기존 테이블이 있다면 삭제
DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS users;

-- 사용자 테이블 생성
CREATE TABLE users (
    id VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50) NOT NULL,
    curriculum_code INT,
    curriculum_name VARCHAR(100)
);

-- 게시판 테이블 생성
CREATE TABLE board (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    writer VARCHAR(50) NOT NULL,
    content TEXT,
    view_cnt INT DEFAULT 0,
    reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 기본 관리자 계정 생성
INSERT INTO users (id, password, name, curriculum_code, curriculum_name)
VALUES ('admin', 'admin', '관리자', 1, '관리자과정');

-- 일반 사용자 계정 생성
INSERT INTO users (id, password, name, curriculum_code, curriculum_name)
VALUES ('ssafy', 'ssafy', '싸피', 2, 'Java과정');
INSERT INTO users (id, password, name, curriculum_code, curriculum_name)
VALUES ('user', 'user', '일반사용자', 3, 'Python과정');

-- 샘플 게시글 데이터
INSERT INTO board (title, writer, content)
VALUES ('첫번째 게시글입니다.', '관리자', '첫번째 게시글의 내용입니다. 환영합니다!');
INSERT INTO board (title, writer, content)
VALUES ('Spring Framework란?', '싸피', 'Spring Framework는 자바 플랫폼을 위한 오픈소스 애플리케이션 프레임워크로서...');
INSERT INTO board (title, writer, content)
VALUES ('게시판 프로젝트 시작합니다', '일반사용자', '게시판 프로젝트를 시작했습니다. 많은 관심 부탁드립니다.');
INSERT INTO board (title, writer, content)
VALUES ('인터셉터와 아규먼트 리졸버', '관리자', '스프링에서 인터셉터와 아규먼트 리졸버를 활용한 인증 처리 방법에 대해 알아봅시다.');
INSERT INTO board (title, writer, content)
VALUES ('Spring Boot 사용법', '싸피', 'Spring Boot를 사용하면 Spring 프로젝트를 빠르게 설정하고 실행할 수 있습니다.');
