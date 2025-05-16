<template>
  <div class="login-wrapper">
    <div class="login-container">
      
      <!-- 로고 -->
      <div class="logo-box">
        <img
          src="/logo.png"
          alt="Meal Tracker Logo"
          class="logo"
        />
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

const form = reactive({
  userId: '',
  userPassword: ''
})

function login() {
  console.log('로그인 시도:', form)
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
