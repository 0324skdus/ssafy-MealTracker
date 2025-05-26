<template>
  <div class="edit-wrapper">
    <div class="edit-container">
      <div class="logo-box">
        <img src="/logo.png" alt="logo" class="logo" />
      </div>

      <div class="form-box">
        <div class="row-box">
          <div class="column-box">
            <label class="label">성별</label>
            <div class="radio-group">
              <label class="radio-box"><input type="radio" value="M" v-model="user.gender" /> 남</label>
              <label class="radio-box"><input type="radio" value="F" v-model="user.gender" /> 여</label>
            </div>
          </div>

          <div class="column-box">
            <label class="label">나이(만)</label>
            <input v-model="user.age" type="number" class="input-box no-spin" />
          </div>
        </div>

        <div class="row-box">
          <div class="column-box">
            <label class="label">키 (cm)</label>
            <input v-model="user.height" type="number" class="input-box no-spin" />
          </div>

          <div class="column-box">
            <label class="label">체중 (kg)</label>
            <input v-model="user.weight" type="number" class="input-box no-spin" />
          </div>
        </div>

        <div class="activity-box">
          <div class="label">평소 활동량이 얼마나 되나요?</div>
          <label><input type="radio" value="L" v-model="user.activityLevel" /> 오래 앉아서 일하고 운동을 거의 하지 않습니다</label>
          <label><input type="radio" value="M" v-model="user.activityLevel" /> 가끔 걷거나 가벼운 활동을 합니다</label>
          <label><input type="radio" value="H" v-model="user.activityLevel" /> 몸을 많이 쓰는 일을 하거나 운동을 규칙적으로 합니다</label>
        </div>

        <div class="activity-box">
          <div class="label">어떤 목표를 가지고 계신가요?</div>
          <label><input type="radio" value="체중감량" v-model="user.specialNote" /> 체중을 감량하고 싶습니다</label>
          <label><input type="radio" value="체중유지" v-model="user.specialNote" /> 현재 체중을 유지하고 싶습니다</label>
          <label><input type="radio" value="근육량 증가" v-model="user.specialNote" /> 근육량을 늘리고 싶습니다</label>
        </div>

        <div class="button-center">
          <button @click="submitUpdate" class="submit-button">수정 완료</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const user = ref({
  userId: '',
  password: '',
  name: '',
  gender: '',
  height: 0,
  weight: 0,
  age: 0,
  specialNote: '',
  activityLevel: ''
})

import { useRouter } from 'vue-router'
const router = useRouter()

onMounted(async () => {
  const token = localStorage.getItem('accessToken')
  const userId = localStorage.getItem('userId')

  if (!token || !userId) {
    alert('로그인이 필요합니다.')
    router.push('/') // 로그인 페이지로 강제 이동
    return
  }

  try {
    const res = await axios.get(`http://localhost:8080/api/user/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    user.value = res.data
  } catch (err) {
    console.error(err)
    alert('회원 정보 조회 실패')
  }
})


async function submitUpdate() {
  const token = localStorage.getItem('accessToken')

  try {
    await axios.put('http://localhost:8080/api/user', user.value, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    alert('회원 정보가 수정되었습니다.')
    router.push('/main')
  } catch (err) {
    console.error(err)
    alert('수정 실패')
  }
}
</script>

<style scoped>
.edit-wrapper {
  width: 100vw;
  height: 100vh;
  background-color: white;
  display: flex;
  justify-content: center;
  align-items: center;
}

.edit-container {
  width: 100%;
  max-width: 28rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1.5rem;
  gap: 1rem;
}

.logo-box {
  width: 100%;
  display: flex;
  justify-content: center;
  margin-bottom: 1rem;
}

.logo {
  width: 10rem;
  max-width: 100%;
  height: auto;
  object-fit: contain;
}

.form-box {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
}

.input-box {
  width: 100%;
  height: 2.5rem;
  border-radius: 0.375rem;
  background-color: #d9f99d;
  padding-left: 1rem;
  font-size: 1rem;
  color: #374151;
  border: none;
}

.no-spin::-webkit-inner-spin-button,
.no-spin::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
.no-spin {
  -moz-appearance: textfield;
}

.row-box {
  display: flex;
  width: 100%;
  gap: 1rem;
}

.column-box {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.label {
  font-weight: bold;
  font-size: 0.875rem;
  color: #111827;
}

.radio-group {
  display: flex;
  gap: 1rem;
}

.radio-box {
  background-color: #d9f99d;
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
}

.activity-box {
  background-color: #d9f99d;
  border-radius: 0.375rem;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.button-center {
  width: 100%;
  display: flex;
  justify-content: center;
}

.submit-button {
  width: 48%;
  height: 2.5rem;
  background-color: #4caf50;
  border-radius: 0.375rem;
  box-shadow: 0 1px 2px rgba(0,0,0,0.1);
  color: #1f2937;
  font-size: 1rem;
  cursor: pointer;
}
</style>
