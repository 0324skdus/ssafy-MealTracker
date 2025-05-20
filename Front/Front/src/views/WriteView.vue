<template>
  <div class="main-layout">
    <LeftSidebar />

    <div class="content-area">
      <header class="header-area">
        <img src="/logo.png" alt="Logo" class="logo" />
      </header>

      <div class="write-container">
        <div class="form-top">
          <select v-model="form.communityType" class="board-type-select">
            <option disabled value="">게시판 선택</option>
            <option>자유 게시판</option>
            <option>식단 게시판</option>
          </select>

          <input
            v-model="form.title"
            type="text"
            placeholder="제목을 입력하세요"
            class="title-input"
          />
        </div>

        <textarea
          v-model="form.content"
          placeholder="내용을 입력하세요"
          class="content-box"
        ></textarea>

        <div class="button-wrapper">
          <button @click="submitPost" class="submit-button">등록</button>
        </div>
      </div>
    </div>

    <RightSidebar />
  </div>
</template>

<script>
import { ref, watch } from 'vue'
import { onBeforeRouteLeave, useRouter } from 'vue-router'
import axios from 'axios'
import LeftSidebar from '@/components/LeftSidebar.vue'
import RightSidebar from '@/components/RightSidebar.vue'

export default {
  name: 'WriteView',
  components: {
    LeftSidebar,
    RightSidebar,
  },
  setup() {
    const router = useRouter()

    const form = ref({
      communityType: '',
      title: '',
      content: '',
    })

    let isDirty = false
    const markDirty = () => { isDirty = true }
    watch(form, markDirty, { deep: true })

    const submitPost = async () => {
      if (!form.value.communityType || !form.value.title || !form.value.content) {
        alert('모든 항목을 작성해주세요.')
        return
      }

      const token = localStorage.getItem('accessToken')
      if (!token) {
        alert("로그인이 필요합니다.")
        return
      }

      try {
        const response = await axios.post('/api/community', { ...form.value }, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        })

        isDirty = false

        alert(response.data)
        router.push('/board')
      } catch (err) {
        alert('게시글 등록 실패')
        console.error(err)
      }
    }

    onBeforeRouteLeave((to, from, next) => {
      if (isDirty && (form.value.title || form.value.content)) {
        const confirmLeave = confirm('작성 중인 내용이 있습니다. 나가시겠습니까?')
        if (confirmLeave) next()
        else next(false)
      } else {
        next()
      }
    })

    return {
      form,
      submitPost,
    }
  },
}
</script>

<style scoped>
.main-layout {
  display: flex;
  min-height: 100vh;
}

.content-area {
  flex: 1;
  padding: 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.header-area {
  margin-bottom: 16px;
  text-align: center;
}

.logo {
  width: 160px;
  height: auto;
}

.write-container {
  width: 100%;
  max-width: 800px;
  background: #fff;
  padding: 24px;
  box-shadow: 0 0 6px rgba(0,0,0,0.1);
  border-radius: 8px;
}

.form-top {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.board-type-select {
  flex: 1;
  padding: 8px;
  font-size: 16px;
}

.title-input {
  flex: 3;
  padding: 8px;
  font-size: 16px;
}

.content-box {
  width: 100%;
  height: 300px;
  padding: 12px;
  font-size: 16px;
  resize: vertical;
  margin-bottom: 24px;
}

.button-wrapper {
  display: flex;
  justify-content: center;
}

.submit-button {
  padding: 12px 32px;
  background-color: #4caf50;
  color: white;
  border: none;
  font-size: 16px;
  border-radius: 6px;
  cursor: pointer;
}
</style>
