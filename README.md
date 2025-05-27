# MealTracker_KIMNAYEON_LEEJONGHYEON
1. 프로젝트 소개
   - 개발 기간: 
   - 프로젝트 내용: 식단기록및 피드백 웹애플리케이션
   - 기술 스택: SpringBoot, MyBatis, Vue
   - 개발 인원: 김나연(팀장),이종현(팀원) 2인

2. 주요 기능


| 기능 번호 | 기능 명 | 설명 |
| ------ | ------ | ------ |
| 1         | 회원가입 / 로그인 / 로그아웃     | 기본적인 인증 처리. 닉네임 중복 체크 포함               |
| 2         | 마이페이지 - 신체정보, 목표 입력 | 나이, 성별, 키, 체중, 목표(감량/유지/증량) 입력          |
| 3         | 식단 등록                        | 식사 시간, 음식 이름, 양 입력 (수기로 시작)              |
| 4         | 식단 분석                        | 입력 식품 → 열량 및 영양소 계산 → 건강 점수 도출         |
| 5         | 식단에 대한 자동 피드백           | 예: “단백질이 부족해요” / “좋은 식단이에요!” 등          |
| 6         | 게시판 - 식단 공유               | 식단 글 등록 + 본인이 분석한 결과 포함                   |
| 7         | 게시판 - 댓글 기능               | 유저 간 피드백과 교류                                   |
| 8         | 게시판 - 추천 기능               | 좋아요 버튼. 추천 수 카운트                             |

3. 서비스 설계
   - 와이어프레임 목업*
![Untitled](/uploads/3565a27c7c12be6c8018b5a42cf23ae2/Untitled.png)
   - 데이터 베이스 모델링 (ERD) *
![ERD-냠냠코치](/uploads/1e807d7bcca22ce9aa328fab9fd3d090/ERD-냠냠코치.png)
   - URL 명세서(or REST API 명세) *  
   - Vue 컴포넌트 구조

```C:\MEALTRACKER\FRONT\FRONT\SRC  
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


  - 팀원 및 업무분담
    김나연: 메인페이지 캘린더 ui구현, 추천기능 구현  
    이종현: 게시판기능 구현, gpt api를 활용한 식단 코칭 기능구현
4. 후기
   - 팀장 김나연: 프로젝트를 직접 구현해보면서 지금까지 배웠던것이 실무에서 어떻게 쓰이는지 파악할 수 있는 시간이었습니다. 또한 처음 프론트 설게를 해보면서 사용자 입장에서 어떤 정보를 보여줘야 직관적이고 편리할지를 연습해볼 수 있었습니다.
   - 팀원 이종현: 초기 설계와 실제 구현 과정 사이에 차이가 발생하면서 일부 기능이 누락되거나 계획이 변경되는 경우가 있었습니다. 이를 통해 사전에 보다 정교하고 구체적인 설계의 필요성을 체감하였습니다.
또한 Git 사용 및 DB 설계 과정에서도 미처 고려하지 못한 요소들이 개발 도중 발견되어 수정이 반복되는 일이 잦았습니다.
향후 프로젝트에서는 이러한 문제를 최소화하기 위해 설계 단계에서 기능과 데이터 구조를 면밀히 검토하고 준비하는 과정이 선행되어야 함을 느꼈습니다.



