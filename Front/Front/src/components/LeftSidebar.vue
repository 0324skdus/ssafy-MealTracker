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
      <li>
        <router-link to="/mypage">
          마이페이지
        </router-link>
        </li><br>

        <li>
          <router-link to="/board">
          게시판
        </router-link>
        </li><br>

      <li @click="logout">로그아웃</li>
    </ul>
  </aside>
</template>

<style scoped>
.left-sidebar {
  width: 200px;
  padding: 20px;
  min-height: 100vh;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  text-align: center;
}
.left-sidebar button {
  background-color: transparent;
  border: none;
  padding: 10px;
  width: 100%;
  text-align: center;
  cursor: pointer;
  transition: background-color 0.3s;
}

li:hover {
  background-color: #b1f584;
  border-radius: 10px;
  transition: background-color 0.3s;
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

li {
  padding: 10px 15px;
  font-weight: bold;
  cursor: pointer;
  border-radius: 8px;
  transition: background-color 0.3s;
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
