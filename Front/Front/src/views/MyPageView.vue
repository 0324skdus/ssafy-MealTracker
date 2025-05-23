<template>
  <div class="main-layout">
    <LeftSidebar />

    <div class="mypage-container">
      <h1 class="title">마이페이지</h1>
      <p class="summary">{{ userName }}님, {{ daysSinceJoined }}일 동안 Meal Tracker를 이용 중입니다.</p>

      <!-- 탭 버튼 -->
      <div class="tab-buttons">
        <button
          v-for="tab in tabs"
          :key="tab.key"
          :class="{ active: selectedTab === tab.key }"
          @click="selectedTab = tab.key"
        >
          {{ tab.label }}
        </button>
      </div>

      <!-- 탭 내용 -->
      <div class="tab-content">
        <div v-if="selectedTab === 'info'" class="info-tab">
          <p><strong>성별:</strong> {{ user.gender }}</p>
          <p><strong>나이:</strong> {{ user.age }}세</p>
          <p><strong>키:</strong> {{ user.height }}cm</p>
          <p><strong>몸무게:</strong> {{ user.weight }}kg</p>
          <p><strong>목표:</strong> {{ convertSpecialNote(user.specialNote) }}</p>
          <p><strong>활동량:</strong> {{ convertActivityLevel(user.activityLevel) }}</p>
        </div>

        <div v-else-if="selectedTab === 'stat'">
          <StatisticsPanel />
        </div>

        <div v-else-if="selectedTab === 'written'" class="post-list">
          <div v-if="myPosts.length === 0">작성한 글이 없습니다.</div>
          <ul v-else>
            <li v-for="post in myPosts" :key="post.id" class="post-item">
              <div class="post-title" @click="goToPost(post.id)">
                {{ post.title }}
              </div>
              <div class="post-meta">작성일: {{ post.createdAt }} | 좋아요: {{ post.likeCount }}</div>
            </li>
          </ul>
        </div>

        <div v-else-if="selectedTab === 'liked'" class="post-list">
          <div v-if="myLikedPosts.length === 0">좋아요 누른 글이 없습니다.</div>
          <ul v-else>
            <li v-for="post in myLikedPosts" :key="post.id" class="post-item">
              <div class="post-title" @click="goToPost(post.id)">
                {{ post.title }}
              </div>
              <div class="post-meta">작성일: {{ post.createdAt }} | 좋아요: {{ post.likeCount }}</div>
            </li>
          </ul>
        </div>
      </div>

      <div v-if="selectedTab === 'info'" class="edit-wrapper">
        <button class="edit-button" @click="goToEditProfile">회원정보 수정</button>
      </div>
    </div>

    <RightSidebar />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import LeftSidebar from '@/components/LeftSidebar.vue'
import RightSidebar from '@/components/RightSidebar.vue'
import StatisticsPanel from '@/components/StatisticsPanel.vue'

const router = useRouter()

const tabs = [
  { key: 'info', label: '내 신체정보' },
  { key: 'stat', label: '내 식단 통계' },
  { key: 'written', label: '내가 쓴 글' },
  { key: 'liked', label: '좋아요 누른 글' }
]

const selectedTab = ref('info')
const user = ref({})
const userName = ref('')
const daysSinceJoined = ref(0)
const myPosts = ref([])
const myLikedPosts = ref([])

const fetchUserInfo = async () => {
  const token = localStorage.getItem('accessToken')
  const userId = localStorage.getItem('userId')
  try {
    const res = await axios.get(`/api/user/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    user.value = res.data
    userName.value = res.data.name

    const joinDate = new Date(res.data.createdAt)
    const now = new Date()
    const diff = Math.floor((now - joinDate) / (1000 * 60 * 60 * 24))
    daysSinceJoined.value = diff
  } catch (err) {
    alert('회원 정보를 불러오지 못했습니다.')
    router.push('/main')
  }
}

const fetchMyPosts = async () => {
  const token = localStorage.getItem('accessToken')
  const userId = localStorage.getItem('userId')
  try {
    const res = await axios.get(`/api/community/user/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    myPosts.value = res.data
  } catch (err) {
    console.error('내 게시글 조회 실패:', err)
  }
}

const fetchLikedPosts = async () => {
  const token = localStorage.getItem('accessToken')
  const userId = localStorage.getItem('userId')
  try {
    const res = await axios.get(`/api/community/liked/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    myLikedPosts.value = res.data
  } catch (err) {
    console.error('좋아요 누른 글 조회 실패:', err)
  }
}

watch(selectedTab, (newTab) => {
  if (newTab === 'written') {
    fetchMyPosts()
  } else if (newTab === 'liked') {
    fetchLikedPosts()
  }
})

const goToEditProfile = () => {
  router.push('/profile-edit')
}

const goToPost = (postId) => {
  router.push(`/community/${postId}`)
}

const convertActivityLevel = (level) => {
  if (level === 'L') return '적음'
  if (level === 'M') return '보통'
  if (level === 'H') return '많음'
  return level
}

const convertSpecialNote = (note) => {
  if (note === '체중유지') return '체중 유지'
  if (note === '체중감량') return '체중 감량'
  if (note === '근육량증가') return '근육량 증가'
  return note
}

onMounted(fetchUserInfo)
</script>

<style scoped>
.main-layout {
  display: flex;
}

.mypage-container {
  flex: 1;
  padding: 40px;
}

.title {
  font-size: 26px;
  margin-bottom: 8px;
}

.summary {
  margin-bottom: 30px;
  font-size: 16px;
  color: #444;
}

.tab-buttons {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.tab-buttons button {
  padding: 8px 16px;
  border: none;
  background-color: #e5e5e5;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
}

.tab-buttons button.active {
  background-color: #4caf50;
  color: white;
}

.tab-content {
  background: #f5f5f5;
  padding: 20px;
  border-radius: 8px;
}

.info-tab p {
  margin-bottom: 12px;
}

.edit-wrapper {
  margin-top: 20px;
}

.edit-button {
  background-color: #86efac;
  border: none;
  padding: 10px 16px;
  border-radius: 6px;
  cursor: pointer;
}

.edit-button:hover {
  background-color: #4ade80;
}

.post-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.post-item {
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 6px;
  padding: 12px;
}

.post-title {
  font-weight: bold;
  margin-bottom: 6px;
  cursor: pointer;
  color: black;
  text-decoration: none;
}

.post-meta {
  font-size: 12px;
  color: #666;
}
</style>
