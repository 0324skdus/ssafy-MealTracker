<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

function logout() {
  localStorage.removeItem('accessToken')  // JWT 토큰 삭제
  alert('로그아웃 되었습니다.')
  router.push('/')                        // index 페이지로 이동
}
</script>


<template>
  <aside class="left-sidebar">
    <div class="logo-box">
      <router-link to="/main">
        <img src="/logo.png" alt="Meal Tracker Logo" class="logo" />
      </router-link>
    </div>
    <ul>
      <router-link to="/mypage">
        <li>마이페이지</li>
      </router-link>

      <router-link to="/board">
        <li>게시판</li>
      </router-link>

      <router-link to="/profile-edit">
        <li>회원정보 수정</li>
      </router-link>

      <li @click="logout">로그아웃</li>
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

ul {
  list-style: none;
  padding: 0;
}

li {
  padding: 10px 0;
  cursor: pointer;
  font-weight: bold;
}

router-link {
  text-decoration: none;
  color: inherit;
}
</style>
