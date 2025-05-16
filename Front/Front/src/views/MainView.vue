<template>
  <div class="main-layout">
    <aside class="sidebar">
      <ul>
        <li>마이페이지</li>
        <li>게시판</li>
        <li>회원정보 수정</li>
      </ul>
    </aside>

    <main class="calendar-area">
      <!-- 여기에 추후 FullCalendar 또는 캘린더 들어감 -->
      <p>여기는 메인페이지입니다.</p>
    </main>
  </div>
</template>

<style scoped>
.main-layout {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: 200px;
  background-color: #c7eeb0;
  padding: 20px;
}

.calendar-area {
  flex-grow: 1;
  padding: 30px;
  background-color: #fff;
}
</style>
