/*Book Market 초기 데이터 파일*/

USE book_market;


/* 1) 관리자 계정 기본 데이터 */

INSERT INTO admins (admin_id, password, name, mobile)
VALUES ('admin', '1234', '관리자', '010-0000-0000');


/* 2) 도서(Book) 기본 데이터 */

INSERT INTO book (book_id, name, unit_price, author, description, category, release_date) VALUES
('ISBN1234', '쉽게 배우는 JSP 웹 프로그래밍', 27000, '송미영', '단계별로 쇼핑몰을 구현하며 배우는 JSP 웹 프로그래밍', 'IT전문서', '2018-10-08'),
('ISBN1235', '안드로이드 프로그래밍', 33000, '우재남', '실습 단계별 명쾌한 멘토링!', 'IT전문서', '2022-01-22'),
('ISBN220913101957', '스크래치', 22000, '고광일', '컴퓨팅 사고력을 키우는 블록 코딩', '컴퓨터입문', '2019-06-10');

/* 3) 테스트용 사용자 데이터 (선택사항) */

INSERT INTO users (name, mobile)
VALUES ('홍길동', '01012341234');