<template>
  <div class="login-wrapper">
    <div class="login-container">
      
      <!-- 로고 -->
      <div class="logo-box">
        <router-link to="/main">
        <img
          src="/logo.png"
          alt="Meal Tracker Logo"
          class="logo"
        />
        </router-link>
      </div>

      <!-- ID 입력 -->
      <input
        v-model="form.userId"
        placeholder="ID"
        class="input-box"
      />

      <!-- PW 입력 -->
      <input
        v-model="form.userPassword"
        type="password"
        placeholder="PW"
        class="input-box"
      />

      <!-- 로그인 버튼 -->
      <button
        @click="login"
        class="login-button"
      >
        로그인
      </button>

      <!-- 개별 링크 버튼들 -->
      <div class="link-group">
        <router-link to="/signup" class="link-button">회원가입</router-link>
        <button class="link-button">아이디 찾기</button>
        <button class="link-button">비밀번호 찾기</button>
      </div>

    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const form = reactive({
  userId: '',
  userPassword: ''
})

async function login() {
  try {
    const response = await axios.post('http://localhost:8080/api/auth/login', {
      userId: form.userId,
      userPassword: form.userPassword
    })

    const { token, userId, name } = response.data
    localStorage.setItem('accessToken', token)
    localStorage.setItem('userId', userId)
    localStorage.setItem('userName', name) // 추가 저장

    router.push('/main')
  } catch (err) {
    alert('로그인 실패: 아이디 또는 비밀번호 확인')
    console.error(err)
  }
}
</script>

<style>
.login-wrapper {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: white;
}

.login-container {
  width: 100%;
  max-width: 24rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.25rem;
  padding: 1.5rem;
}

.logo-box {
  width: 100%;
  display: flex;
  justify-content: center;
  margin-top: 1.5rem;
  margin-bottom: 0.5rem;
}

.logo {
  width: 9rem;
  height: auto;
  object-fit: contain;
}

.input-box {
  width: 90%;
  height: 2.75rem;
  background-color: #d9f99d;
  border-radius: 0.375rem;
  padding-left: 1rem;
  color: #374151;
  font-size: 1rem;
}

.login-button {
  width: 90%;
  height: 2.75rem;
  background-color: #bef264;
  border-radius: 0.375rem;
  box-shadow: 0 1px 2px rgba(0,0,0,0.1);
  color: #1f2937;
  font-size: 1rem;
  cursor: pointer;
}

.link-group {
  width: 100%;
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 0.25rem;
}

.link-button {
  color: #65a30d;
  font-size: 0.875rem;
  text-decoration: none;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
}

.link-button:hover {
  text-decoration: underline;
}
</style>
