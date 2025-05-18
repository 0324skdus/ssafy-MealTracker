<template>
  <aside class="left-sidebar">
    <div class="logo-box">
      <router-link to="/main">
        <img src="/logo.png" alt="Meal Tracker Logo" class="logo" />
      </router-link>
    </div>
    <ul>
      <li><router-link to="#">마이페이지</router-link></li>
      <li><router-link to="#">게시판</router-link></li>
      <li><router-link to="/profile-edit">회원정보 수정</router-link></li>
    </ul>
  </aside>
</template>

<style scoped>
.left-sidebar {
  width: 200px;
  background-color: #c7eeb0;
  padding: 20px;
}
.logo-box {
  margin-bottom: 16px;
  text-align: center;
}

.logo {
  width: 140px;
  height: auto;
  margin-top: 10px;
  margin-bottom: 10px;
  cursor: pointer;
}
</style>
