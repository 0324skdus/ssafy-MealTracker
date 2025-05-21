<template>
  <div class="main-layout">
    <LeftSidebar />

    <div class="content-area">
      <div v-if="post" class="post-container">
        <h1 class="post-title">{{ post.title }}</h1>

        <div class="post-meta">
          <span>작성자: {{ post.userId }}</span>
          <span>| 게시판 분류: {{ post.communityType }}</span>
          <span>| 작성일: {{ formatDate(post.createdAt) }}</span>
        </div>

        <div class="post-content-box">
          <p class="post-content">{{ post.content }}</p>
        </div>

        <div class="post-actions">
          <div class="left-group">
            <button class="like-button" @click="likePost">❤️ 좋아요 {{ post.likeCount }}</button>
          </div>

          <div class="right-group" v-if="isAuthor">
            <button class="edit-button" @click="goToEdit">수정</button>
            <button class="delete-button" @click="deletePost">삭제</button>
          </div>
        </div>
      </div>

      <div v-else class="loading">게시글을 불러오는 중입니다...</div>
    </div>

    <RightSidebar />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import LeftSidebar from '@/components/LeftSidebar.vue'
import RightSidebar from '@/components/RightSidebar.vue'

const route = useRoute()
const router = useRouter()
const postId = route.params.id
const post = ref(null)
const isAuthor = ref(false)

const fetchPost = async () => {
  try {
    const token = localStorage.getItem('accessToken')
    const res = await axios.get(`/api/community/${postId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    post.value = res.data

    const decoded = parseJwt(token)
    const loggedInUserId = (decoded?.userId || decoded?.sub || '').trim().toLowerCase()
    const postOwnerId = (res.data.userId || '').trim().toLowerCase()

    isAuthor.value = loggedInUserId === postOwnerId
  } catch (err) {
    alert('게시글을 불러오지 못했습니다.')
    router.push('/board')
  }
}

const likePost = async () => {
  const token = localStorage.getItem('accessToken')
  try {
    const response = await axios.post(`/api/community/${postId}/like`, {}, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    const message = response.data === '좋아요 등록' ? '좋아요 완료!' : '좋아요 취소!'
    alert(message)
    fetchPost()
  } catch (err) {
    alert('좋아요 실패')
  }
}

const deletePost = async () => {
  if (!confirm('정말 삭제하시겠습니까?')) return
  const token = localStorage.getItem('accessToken')
  try {
    await axios.delete(`/api/community/${postId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    alert('삭제 완료')
    router.push('/board')
  } catch (err) {
    alert('삭제 실패')
  }
}

const goToEdit = () => {
  router.push(`/edit/${postId}`)
}

const formatDate = (str) => str?.slice(0, 10)

const parseJwt = (token) => {
  try {
    const base64 = token.split('.')[1]
    const decoded = JSON.parse(decodeURIComponent(escape(atob(base64))))
    return decoded
  } catch (e) {
    return null
  }
}

onMounted(fetchPost)
</script>

<style scoped>
.main-layout {
  display: flex;
}
.content-area {
  flex: 1;
  display: flex;
  justify-content: center;
  padding: 30px 0;
}
.post-container {
  width: 700px;
}
.post-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 10px;
}
.post-meta {
  color: #666;
  font-size: 14px;
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}
.post-content-box {
  border: 1px solid #ccc;
  padding: 20px;
  background-color: #fafafa;
  border-radius: 6px;
  margin-bottom: 30px;
}
.post-content {
  font-size: 16px;
  line-height: 1.6;
  white-space: pre-wrap;
}

.post-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
}
.left-group,
.right-group {
  display: flex;
  gap: 12px;
}

/* 버튼 통일 색상 */
.like-button,
.edit-button,
.delete-button {
  background-color: #86efac;
  border: none;
  border-radius: 6px;
  padding: 8px 16px;
  font-size: 14px;
  cursor: pointer;
  transition: 0.2s;
}

.like-button:hover,
.edit-button:hover,
.delete-button:hover {
  background-color: #4ade80;
}

.loading {
  padding: 40px;
  text-align: center;
  font-size: 18px;
}
</style>
