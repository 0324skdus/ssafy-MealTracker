<template>
  <aside class="left-sidebar">
    <div class="logo-box">
      <router-link to="/main">
        <img src="/logo.png" alt="Meal Tracker Logo" class="logo" />
      </router-link>
    </div>
    <ul>
      <router-link to="/main" >
        <li>마이페이지</li>
      </router-link >
      
      <router-link to="/board" >
        <li>게시판</li>
      </router-link>
      <router-link to ="/profile-edit">
        <li>회원정보 수정</li>
      </router-link>
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
