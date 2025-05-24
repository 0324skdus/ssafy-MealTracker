<template>
  <div class="notification-dropdown">
    <div class="dropdown-header">
      <strong>알림</strong>
      <div class="header-buttons">
        <button class="mark-read-btn" @click="markAllAsRead">전체읽음</button>
        <button class="close-btn" @click="handleClose">X</button>
      </div>
    </div>
    <ul>
      <li v-if="notifications.length === 0" class="empty">알림이 없습니다.</li>
      <li
        v-for="noti in notifications"
        :key="noti.id"
        class="notification-item"
      >
        <div class="noti-content" @click="handleClick(noti)">
          <div class="content">{{ noti.content }}</div>
          <div class="time">{{ formatTime(noti.createdAt) }}</div>
        </div>
        <button class="delete-btn" @click.stop="() => deleteNotification(noti.id)">✕</button>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const emit = defineEmits(['close', 'update-count'])

const notifications = ref([])
const router = useRouter()
const token = localStorage.getItem('accessToken')

// 알림 목록 조회
async function fetchNotifications() {
  try {
    const res = await axios.get('http://localhost:8080/api/notification/list', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    notifications.value = res.data
  } catch (e) {
    console.error('알림 불러오기 실패:', e)
  }
}

// 시간 포맷
function formatTime(dateTimeStr) {
  const date = new Date(dateTimeStr)
  return date.toLocaleString()
}

// 알림 클릭 (읽음 처리 + 이동 + 뱃지 감소)
async function handleClick(noti) {
  if (!noti.communityId) return

  try {
    await axios.put(`http://localhost:8080/api/notification/read/${noti.id}`, {}, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })

    notifications.value = notifications.value.map(n =>
      n.id === noti.id ? { ...n, isRead: true } : n
    )

    emit('update-count')
    emit('close')
    router.push(`/community/${noti.communityId}`)
  } catch (e) {
    console.error('알림 읽음 처리 실패:', e)
  }
}

// 알림 단건 삭제
async function deleteNotification(id) {
  try {
    await axios.delete(`http://localhost:8080/api/notification/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    notifications.value = notifications.value.filter(n => n.id !== id)
    emit('update-count')
  } catch (e) {
    console.error('알림 삭제 실패:', e)
  }
}

// 전체읽음
async function markAllAsRead() {
  try {
    await axios.put('http://localhost:8080/api/notification/read/all', {}, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    notifications.value = []
    emit('update-count')
  } catch (e) {
    console.error('전체 읽음 실패:', e)
  }
}

// 닫기
function handleClose() {
  emit('close')
}
onMounted(fetchNotifications)
</script>

<style scoped>
.notification-dropdown {
  position: absolute;
  top: 65px;
  right: 0;
  width: 320px;
  background: white;
  border: 1px solid #ccc;
  border-radius: 8px;
  z-index: 2000;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.dropdown-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #eee;
  background: #f8f8f8;
}

.header-buttons {
  display: flex;
  gap: 6px;
}

.mark-read-btn,
.close-btn,
.delete-btn {
  background: none;
  border: none;
  font-weight: bold;
  font-size: 14px;
  cursor: pointer;
}

.mark-read-btn {
  color: #007bff;
}

.notification-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}

.notification-item:hover {
  background-color: #f6f6f6;
}

.noti-content {
  flex: 1;
  padding-right: 6px;
}

.content {
  font-size: 14px;
}

.time {
  font-size: 12px;
  color: gray;
  margin-top: 4px;
}

.empty {
  padding: 20px;
  text-align: center;
  color: #aaa;
  font-size: 18px;
  font-weight: bold;
}
</style>
