<template>
  <div class="login-wrapper">
    <div class="login-container">
      <!-- 로고 -->
      <div class="logo-box">
        <router-link to="/main">
          <img src="/logo.png" alt="Meal Tracker Logo" class="logo" />
        </router-link>
      </div>

      <!-- 아이디 찾기 -->
      <h2>아이디 찾기</h2>
      <input v-model="name" placeholder="이름" class="input-box" />
      <input v-model="email" placeholder="이메일" class="input-box" />

      <button @click="findId" class="login-button">아이디 찾기</button>

      <p v-if="result">아이디는 <strong>{{ result }}</strong> 입니다.</p>
      <p v-if="error" style="color: red;">{{ error }}</p>

      <div class="link-group">
        <router-link to="/" class="link-button">로그인</router-link>
        <router-link to="/find-password" class="link-button">비밀번호 찾기</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const name = ref('')
const email = ref('')
const result = ref('')
const error = ref('')

const findId = async () => {
  try {
    const res = await axios.post('http://localhost:8080/api/find/id', {
      name: name.value,
      email: email.value
    })
    result.value = res.data
    error.value = ''
  } catch (err) {
    result.value = ''
    error.value = '일치하는 정보가 없습니다.'
  }
}
</script>

<style scoped>
.login-wrapper {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: white;
}
</style>
