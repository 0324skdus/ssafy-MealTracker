<template>
  <div class="main-layout">
    <LeftSidebar />

    <div class="content-area">
      <header class="header-area">
      </header>

      <div class="search-area">
        <div class="search-wrapper">
          <span class="search-icon">🔍</span>
          <input
            v-model="searchKeyword"
            placeholder="식단 제목 검색"
            class="search-box"
          />
        </div>
      </div>

      <main class="board-area">
        <div class="board-list">
          <div class="board-item" v-for="post in pagedPosts" :key="post.id">
            <span class="title" :title="post.title">{{ post.title }}</span>
            <span class="likes">❤️ {{ post.likeCount }}</span>
            <span class="writer">{{ maskUsername(post.userId) }}</span>
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
      </main>
    </div>

    <RightSidebar />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import LeftSidebar from '@/components/LeftSidebar.vue'
import RightSidebar from '@/components/RightSidebar.vue'

//게시글 수가 많지않은 상황이라 전체글정보를 가져와서 프론트선에서 잘라서 페이지네이션 표시, 여유생기면 서버페이지네이션 도전
//더미데이터
const posts = ref([
  { id: 1, userId: 'ssafy01', title: '5월 18일 점심식단 여기 제목이 몇글자까지 표시되는지 궁금하네요', likeCount: 4, createdAt: '2025-05-18' },
  { id: 2, userId: 'healthking', title: '헬스인들을 위한 고단백 저녁', likeCount: 7, createdAt: '2025-05-18' },
  { id: 3, userId: 'sunny33', title: '가벼운 아침 식단 추천', likeCount: 2, createdAt: '2025-05-17' },
  { id: 4, userId: 'leejin', title: '탄단지 균형 점심식단', likeCount: 5, createdAt: '2025-05-16' },
  { id: 5, userId: 'ssafy08', title: '다이어트 도시락 구성', likeCount: 10, createdAt: '2025-05-15' },
  { id: 6, userId: 'testuser', title: '식단 공유합니다', likeCount: 1, createdAt: '2025-05-14' }
])

const searchKeyword = ref("")
const currentPage = ref(1)
const pageSize = 3

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

function maskUsername(userId) {
  return userId.length <= 4 ? '*'.repeat(userId.length) : userId.slice(0, 4) + '*'.repeat(3)
}
</script>

<style scoped>
.main-layout {
  display: flex;
}
.content-area {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.header-area {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}
.search-area {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}
.search-wrapper {
  position: relative;
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
.board-area {
  padding: 0 20px;
}
.board-item {
  display: flex;
  justify-content: space-between;
  padding: 8px;
  border-bottom: 1px solid #ddd;
}
.title {
  flex: 2;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.likes {
  flex: 0.5;
  text-align: center;
}
.writer {
  flex: 1;
  text-align: right;
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
  background-color: #65a30d;
  color: white;
}
</style>
