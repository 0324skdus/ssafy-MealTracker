<template>
  <div class="main-layout">
    <LeftSidebar />

    <div class="content-area">
      <header class="header-area">
        <h2 class="board-title">게시판</h2>
        <div class="board-tabs">
          <button
            v-for="type in tabTypes"
            :key="type"
            :class="{ active: currentTab === type }"
            @click="onTabChange(type)"
          >
            {{ type }}
          </button>
        </div>
      </header>

      <main class="board-area">
        <div class="board-list">
          <div class="board-item" v-for="post in pagedPosts" :key="post.id">
            <router-link :to="`/community/${post.id}`" class="title" :title="post.title">
              {{ post.title }}
            </router-link>
            <span class="likes">❤️ {{ post.likeCount }}</span>
            <span class="writer">{{ maskUsername(post.userId) }}</span>
            <span class="date">{{ formatDate(post.createdAt) }}</span>
          </div>
        </div>

        <div class="pagination">
          <button
            v-for="n in totalPages"
            :key="n"
            @click="currentPage = n"
            :class="{ active: currentPage === n }"
          >
            {{ n }}
          </button>
        </div>

        <div class="action-bar">
          <div class="search-wrapper">
            <span class="search-icon">🔍</span>
            <input
              v-model="searchKeyword"
              placeholder="제목 검색"
              class="search-box"
            />
          </div>
          <button class="write-button" @click="goToWrite">글쓰기</button>
        </div>
      </main>
    </div>

    <RightSidebar />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import LeftSidebar from '@/components/LeftSidebar.vue'
import RightSidebar from '@/components/RightSidebar.vue'

const router = useRouter()
function goToWrite() {
  router.push('/write')
}

const tabTypes = ['인기글', '최신글', '식단', '자유게시판']
const currentTab = ref('최신글')
const currentPage = ref(1)
const pageSize = 10
const searchKeyword = ref("")

const posts = ref([])

const fetchPosts = async () => {
  const token = localStorage.getItem('accessToken')
  try {
    const response = await axios.get(`/api/community?sort=${currentTab.value}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    posts.value = response.data
  } catch (err) {
    console.error('게시글 불러오기 실패', err)
  }
}

onMounted(fetchPosts)
watch(currentTab, () => {
  currentPage.value = 1
  fetchPosts()
})

const filteredPosts = computed(() =>
  posts.value.filter(post =>
    post.title.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
)

const totalPages = computed(() =>
  Math.ceil(filteredPosts.value.length / pageSize)
)

const pagedPosts = computed(() =>
  filteredPosts.value.slice((currentPage.value - 1) * pageSize, currentPage.value * pageSize)
)

function onTabChange(type) {
  currentTab.value = type
}

function maskUsername(userId) {
  return userId.length <= 4 ? '*'.repeat(userId.length) : userId.slice(0, 4) + '*'.repeat(3)
}

function formatDate(dateString) {
  return dateString?.slice(0, 10)
}
</script>

<style scoped>
.main-layout {
  display: flex;
  min-height: 100vh; 
  align-items: stretch; 
}
.content-area {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.header-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
}
.board-title {
  font-size: 42px;
  font-weight: bold;
  margin-bottom: 16px;
}
.board-tabs {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}
.board-tabs button {
  padding: 8px 16px;
  border: none;
  border-radius: 9999px;
  background-color: #eee;
  cursor: pointer;
  font-size: 16px;
}
.board-tabs button.active {
  background-color: #4caf50;
  color: white;
}
.board-area {
  padding: 0 20px;
}
.board-list {
  border-top: 1px solid #ccc;
}
.board-item {
  display: flex;
  justify-content: space-between;
  padding: 8px;
  border-bottom: 1px solid #ddd;
}
.title {
  flex: 3;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.likes {
  flex: 0.7;
  text-align: center;
}
.writer {
  flex: 1;
  text-align: right;
}
.date {
  flex: 1;
  text-align: right;
  color: #666;
  font-size: 14px;
}
.pagination {
  margin-top: 16px;
  text-align: center;
}
.pagination button {
  margin: 0 4px;
  padding: 6px 10px;
  border: none;
  border-radius: 4px;
  background-color: #eee;
  cursor: pointer;
}
.pagination button.active {
  background-color: #4caf50;
  color: white;
}
.action-bar {
  position: relative;
  height: 60px;
  margin: 30px 20px 40px;
}
.search-wrapper {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  width: 50%;
  min-width: 300px;
}
.search-box {
  width: 100%;
  padding: 10px 14px;
  padding-left: 40px;
  border: 1px solid #ccc;
  border-radius: 9999px;
  font-size: 16px;
  outline: none;
}
.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 16px;
  color: #888;
}
.write-button {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-70%);
  padding: 10px 20px;
  border: none;
  border-radius: 9999px;
  background-color: #4caf50;
  color: white;
  font-size: 16px;
  cursor: pointer;
}
</style>
