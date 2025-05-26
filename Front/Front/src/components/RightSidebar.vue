<template>
  <aside class="right-sidebar">
    <br>
    <div class="card">
      <h3>🔥 인기글 🔥</h3>
      <ul>
        <li v-for="post in popularPosts" :key="post.id">
          <router-link :to="`/community/${post.id}`">{{ post.title }}</router-link>
        </li>
      </ul>
    </div>
    <br>
    <div class="card">
      <h3>🌱 최신글 🌱</h3>
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
  width: 300px;
  padding: 16px;
  background-color: #fff;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.card {
  background-color: #e8ece6;
  padding: 14px 16px;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.5);
  height: 260px;
}

.card h3 {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
  text-align: center;
}

.card ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.card ul li {
  font-size: 14px;
  color: #444;
  padding: 6px 4px;
  cursor: pointer;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: color 0.2s;
}

.card ul li:hover {
  color: #2e6ff2;
  text-decoration: underline;
}
.card ul li + li {
  border-top: 1px dotted #9fa19e;
  margin-top: 4px;
  padding-top: 6px;
}
</style>
