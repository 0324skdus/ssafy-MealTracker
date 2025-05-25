<template>
  <div class="edit-container">
    <h2>회원정보 수정</h2>
    <input v-model="form.newUserId" placeholder="아이디" class="input-box" />
    <input v-model="form.name" placeholder="이름" class="input-box" />
    <input v-model="form.email" placeholder="이메일" class="input-box" />

    <input v-model="form.password" type="password" placeholder="새 비밀번호" class="input-box" />
    <input v-model="passwordConfirm" type="password" placeholder="비밀번호 확인" class="input-box" />
    <p v-if="form.password && form.password !== passwordConfirm" class="warning">비밀번호가 일치하지 않습니다.</p>

    <button class="submit-button" @click="submit" :disabled="form.password && form.password !== passwordConfirm">
      변경
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const form = ref({
  userId: '',
  newUserId: '',
  name: '',
  email: '',
  password: ''
})
const passwordConfirm = ref('')

onMounted(async () => {
  form.value.userId = localStorage.getItem('userId') || ''
  const token = localStorage.getItem('accessToken') || '' 
  try {
    const res = await axios.get(`/api/user/${form.value.userId}`,{
         headers: { Authorization: `Bearer ${token}` }
    })
    form.value.name = res.data.name
    form.value.email = res.data.email
    form.value.newUserId = res.data.userId
  } catch (err) {
    alert('회원정보 조회 실패')
    router.push('/mypage')
  }
})

const submit = async () => {
  const token = localStorage.getItem('accessToken') || ''
  const payload = {
    userId: form.value.userId,
    newUserId: form.value.newUserId !== form.value.userId ? form.value.newUserId : undefined,
    name: form.value.name,
    email: form.value.email,
    password: form.value.password || undefined
  }

  try {
    await axios.put('http://localhost:8080/api/user/profile/update', payload, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    })

    alert('회원 정보가 수정되었습니다.')
    if (payload.newUserId) {
      localStorage.setItem('userId', payload.newUserId)
    }
    router.push('/mypage')
  } catch (err) {
    alert('정보 수정 실패')
    console.error('[ERROR] axios 실패', err)
  }
}
</script>

<style scoped>
.edit-container {
  max-width: 400px;
  margin: 40px auto;
  padding: 20px;
  background: #f8f8f8;
  border-radius: 8px;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);
}
.input-box {
  width: 100%;
  padding: 10px;
  margin-bottom: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}
.submit-button {
  width: 100%;
  padding: 10px;
  background-color: #4caf50;
  color: white;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.submit-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
.warning {
  color: red;
  font-size: 12px;
  margin-bottom: 10px;
}
</style>
