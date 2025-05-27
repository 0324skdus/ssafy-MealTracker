# MealTracker_KIMNAYEON_LEEJONGHUN
1. 프로젝트 소개
   - 개발 기간: 2025.04.26 ~ 2025.05.26
   - 프로젝트 내용: 식단기록및 피드백 웹애플리케이션
   - 기술 스택: SpringBoot, MyBatis, Vue
   - 개발 인원: 2명

2. 주요 기능

🔧 전체 아키텍처
Frontend: Vue.js 3 (Composition API + Option API 병행)

Backend: Spring Boot

DBMS: MySQL

ORM: MyBatis

API 문서화: Swagger (OpenAPI 3.0)

인증: JWT(Json Web Token) 기반 인증/인가

통신: REST API

프론트 서버 실행: Vite

🧑‍🍳 기능별 기술 구성
📌 회원 관리
회원가입 (아이디 중복검사 포함)

로그인 (JWT 발급)

아이디/비밀번호 찾기

마이페이지 (캘린더 + 식단 점수 아이콘 표시)

🍽 식단 기능
식단 등록 (식사 종류: 아침, 점심, 저녁, 간식)

식품 검색 + 상세 영양정보 조회

식단별 총 섭취 칼로리 및 탄/단/지/식이섬유/당류 계산

사용자 신체정보 기반 권장 영양소량 비교

식단 점수 환산

📆 캘린더 UI
FullCalendar 사용

날짜 클릭 시 식단 상세 패널 슬라이딩

월별 식단 데이터 요약 조회

🧾 게시판
게시글 작성/수정/삭제

댓글 기능

좋아요 기능

인기글/최신글 구분 (오른쪽 사이드바)

게시글 공유 기능

📊 통계 기능
일간/월간 식단 요약

바그래프(섭취 vs 권장 영양소)

🔔 기타
알림 기능

사이드바: 좌측 메뉴 / 우측 인기글·최신글

GPT API 연동 기능 (식단 피드백)

3. 서비스 설계
   - 목업
![Untitled](/uploads/3565a27c7c12be6c8018b5a42cf23ae2/Untitled.png)
   - 데이터 베이스 모델링 (ERD) 
![MT_ERD](/uploads/67cf8570e291588b6a4adda44eb7b83a/MT_ERD.png)
   - URL 명세서(or REST API 명세) 
[MealTracker_api.pdf](/uploads/4e56eeabfa6f302ca063881d2590ff7f/MealTracker_api.pdf)
   - Vue 컴포넌트 구조

```
C:\MEALTRACKER\FRONT\FRONT\SRC  
|   App.vue
|   main.js
|
+---assets
|       index.css
|
+---components
|       DietDetailPanel.vue : 식단 상세조회를 위한 슬라이딩 패널
|       DietPreviewList.vue :식단 등록페이지-등록할 식단 미리보기
|       DietRegisterPanel.vue : 식단 등록 슬라이딩 패널
|       FeedbackPopup.vue :gpt 피드백 팝업화면
|       LeftSidebar.vue : 메뉴 사이드바
|       Notification.vue : 알림
|       RegisteredDietList.vue
|       RightSidebar.vue : 게시글 인기,최신글 상위 5개 노출하는 사이드바
|       SearchResultList.vue : 식단 등록 페이지- 검색어와 일치하는 식단 리스트 보기
|       SelectedFoodInfo.vue : 식단 등록 페이지- 등록할 식품의 상세 영양정보
|       StatisticsPanel.vue : 기록한 식단 통계 패널
|
+---router
|       index.js
|
\---views
        BoardView.vue : 게시판 메인화면
        CommunityDetail.vue : 게시글 상세화면
        FindIdView.vue  : 아이디찾기
        FindPasswordView.vue : 비밀번호 찾기
        LoginView.vue : 로그인화면
        MainView.vue : 캘린더 기반 식단 관리 메인화면
        MyPageView.vue :마이페이지
        ProfileEditView.vue : 신체정보 입력 및 수정 페이지
        SignUpView.vue : 회원가입페이지
        UserInfoEditView.vue : 회원정보 수정 페이지
        WriteView.vue : 게시글 작성 화면 
```

   - 팀원 및 업무분담
    김나연: 메인페이지 캘린더 ui구현, 추천기능 구현  
    이종현: 게시판기능 구현, gpt api를 활용한 식단 코칭 기능구현
4. 후기
   - 팀장 김나연: 프로젝트를 직접 구현해보면서 지금까지 배웠던것이 실무에서 어떻게 쓰이는지 파악할 수 있는 시간이었습니다. 또한 처음 프론트 설게를 해보면서 사용자 입장에서 어떤 정보를 보여줘야 직관적이고 편리할지를 연습해볼 수 있었습니다.
   - 팀원 이종현: 초기 설계와 실제 구현 과정 사이에 차이가 발생하면서 일부 기능이 누락되거나 계획이 변경되는 경우가 있었습니다. 이를 통해 사전에 보다 정교하고 구체적인 설계의 필요성을 체감하였습니다.
또한 Git 사용 및 DB 설계 과정에서도 미처 고려하지 못한 요소들이 개발 도중 발견되어 수정이 반복되는 일이 잦았습니다.
향후 프로젝트에서는 이러한 문제를 최소화하기 위해 설계 단계에서 기능과 데이터 구조를 면밀히 검토하고 준비하는 과정이 선행되어야 함을 느꼈습니다.



