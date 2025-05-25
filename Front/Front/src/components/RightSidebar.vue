<template>
  <aside class="right-sidebar">
    <div class="info-box">
      <h3>인기글</h3>
      <ul>
        <li v-for="post in popularPosts" :key="post.id">
          <router-link :to="`/community/${post.id}`">{{ post.title }}</router-link>
        </li>
      </ul>
    </div>
    <div class="info-box">
      <h3>최신글</h3>
      <ul>
        <li v-for="post in recentPosts" :key="post.id">
          <router-link :to="`/community/${post.id}`">{{ post.title }}</router-link>
        </li>
      </ul>
    </div>
  </aside>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const popularPosts = ref([])
const recentPosts = ref([])

const fetchPosts = async () => {
  try {
    const token = localStorage.getItem('accessToken')
    if (!token) {
      console.warn('토큰 없음')
      return
    }

    const [popularRes, recentRes] = await Promise.all([
      axios.get('/api/community?sort=인기글', {
        headers: { Authorization: `Bearer ${token}` }
      }),
      axios.get('/api/community?sort=최신글', {
        headers: { Authorization: `Bearer ${token}` }
      })
    ])

    popularPosts.value = popularRes.data.slice(0, 5)
    recentPosts.value = recentRes.data.slice(0, 5)

  } catch (error) {
    console.error('사이드바 게시글 로드 실패:', error)
  }
}

onMounted(fetchPosts)
</script>

<style scoped>
.right-sidebar {
  width: 250px;
  background-color: #c7eeb0;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  min-height: 100vh;
}

.info-box h3 {
  margin-bottom: 10px;
  font-weight: bold;
}

.info-box ul {
  list-style: none;
  padding: 0;
  font-size: 14px;
}

.info-box li {
  margin-bottom: 6px;
}
</style>
