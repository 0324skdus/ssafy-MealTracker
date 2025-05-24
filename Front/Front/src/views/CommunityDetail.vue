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

        <!-- 댓글 -->
        <div class="comment-section">
          <h3 class="comment-title">댓글</h3>

          <!-- 입력창 -->
          <div class="comment-input-box">
            <textarea
              v-model="newComment"
              placeholder="댓글을 작성해주세요."
              maxlength="3000"
              rows="4"
            ></textarea>

            <div class="comment-bottom-row">
              <label class="secret-checkbox">
                <input type="checkbox" v-model="isSecret" />
                비밀댓글
              </label>

              <div class="comment-submit-row">
                <span class="char-count">{{ newComment.length }}/3000</span>
                <button @click="submitComment" class="comment-submit-btn">등록</button>
              </div>
            </div>
          </div>

          <!-- 목록 -->
          <ul class="comment-list">
            <li v-for="comment in comments" :key="comment.id" class="comment-item">
              <div class="comment-meta">
                <span class="comment-author">{{ comment.userId }}</span>
                <span class="comment-date">{{ formatDate(comment.createdAt) }}</span>
              </div>

              <div v-if="editingCommentId === comment.id" class="comment-edit-box">
                <textarea v-model="editingContent" rows="3" class="comment-edit-textarea"></textarea>
                <div class="comment-edit-actions">
                  <button class="comment-edit-confirm-btn" @click="confirmEdit(comment.id)">수정 완료</button>
                  <button class="comment-edit-cancel-btn" @click="cancelEdit">취소</button>
                </div>
              </div>

              <div v-else class="comment-content">
                <template v-if="!comment.secret || isMyComment(comment)">
                  {{ comment.content }}
                </template>
                <template v-else>
                  🔒 비밀댓글입니다.
                </template>
              </div>

              <div class="comment-actions" v-if="isMyComment(comment)">
                <button class="comment-edit-btn" @click="editComment(comment)">수정</button>
                <button class="comment-delete-btn" @click="deleteComment(comment.id)">삭제</button>
              </div>
            </li>
          </ul>

          <div v-if="comments.length === 0" class="no-comments">아직 댓글이 없습니다.</div>
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

const newComment = ref('')
const isSecret = ref(false)
const comments = ref([])
const loggedInUserId = ref('')

const editingCommentId = ref(null)
const editingContent = ref('')

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
    loggedInUserId.value = (decoded?.userId || decoded?.sub || '').trim().toLowerCase()
    isAuthor.value = loggedInUserId.value === post.value.userId.toLowerCase()
  } catch (err) {
    alert('게시글을 불러오지 못했습니다.')
    router.push('/board')
  }
}

const fetchComments = async () => {
  try {
    const token = localStorage.getItem('accessToken')
    const res = await axios.get(`/api/comment/post/${postId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    comments.value = res.data
  } catch (err) {
    alert('댓글을 불러오는 데 실패했습니다.')
  }
}

const submitComment = async () => {
  if (!newComment.value.trim()) {
    alert('댓글 내용을 입력해주세요.')
    return
  }
  const token = localStorage.getItem('accessToken')
  try {
    await axios.post('/api/comment', {
      communityId: postId,
      content: newComment.value,
      secret: isSecret.value
    }, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    alert('댓글이 등록되었습니다.')
    newComment.value = ''
    isSecret.value = false
    await fetchComments()
  } catch (err) {
    alert('댓글 등록에 실패했습니다.')
  }
}

const deleteComment = async (commentId) => {
  if (!confirm('댓글을 삭제하시겠습니까?')) return
  const token = localStorage.getItem('accessToken')
  try {
    await axios.delete(`/api/comment/${commentId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    alert('댓글이 삭제되었습니다.')
    await fetchComments()
  } catch (err) {
    alert('댓글 삭제에 실패했습니다.')
  }
}

const editComment = (comment) => {
  editingCommentId.value = comment.id
  editingContent.value = comment.content
}

const cancelEdit = () => {
  editingCommentId.value = null
  editingContent.value = ''
}

const confirmEdit = async (commentId) => {
  const token = localStorage.getItem('accessToken')
  try {
    await axios.put('/api/comment', {
      id: commentId,
      content: editingContent.value
    }, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    alert('댓글이 수정되었습니다.')
    editingCommentId.value = null
    editingContent.value = ''
    await fetchComments()
  } catch (err) {
    alert('댓글 수정에 실패했습니다.')
  }
}

const isMyComment = (comment) => {
  return (comment.userId || '').toLowerCase() === loggedInUserId.value
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

onMounted(async () => {
  await fetchPost()
  await fetchComments()
})
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

/* 댓글 */
.comment-section {
  margin-top: 50px;
}
.comment-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 14px;
}
.comment-input-box textarea {
  width: 100%;
  resize: none;
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
  font-size: 14px;
}
.comment-bottom-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 6px;
}
.secret-checkbox {
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 4px;
}
.comment-submit-row {
  display: flex;
  align-items: center;
  gap: 10px;
}
.char-count {
  font-size: 12px;
  color: #888;
}
.comment-submit-btn {
  background-color: #86efac;
  border: none;
  border-radius: 6px;
  padding: 6px 14px;
  font-size: 14px;
  cursor: pointer;
}
.comment-submit-btn:hover {
  background-color: #4ade80;
}
.comment-list {
  margin-top: 30px;
  list-style: none;
  padding: 0;
}
.comment-item {
  border-bottom: 1px solid #ddd;
  padding: 12px 0;
}
.comment-meta {
  font-size: 13px;
  color: #555;
  margin-bottom: 4px;
}
.comment-author {
  font-weight: bold;
}
.comment-date {
  margin-left: 12px;
  font-size: 12px;
  color: #888;
}
.comment-content {
  font-size: 15px;
  margin-bottom: 4px;
}
.comment-actions {
  display: flex;
  gap: 6px;
  margin-top: 4px;
}
.comment-delete-btn,
.comment-edit-btn {
  font-size: 12px;
  background-color: transparent;
  border: none;
  cursor: pointer;
}
.comment-delete-btn {
  color: #e11d48;
}
.comment-edit-btn {
  color: #0ea5e9;
}
.no-comments {
  margin-top: 20px;
  color: #999;
  font-size: 14px;
}
.comment-edit-box {
  margin-top: 8px;
  border: 1px solid #ccc;
  border-radius: 6px;
  padding: 12px;
  background-color: #f9f9f9;
}
.comment-edit-textarea {
  width: 100%;
  resize: none;
  font-size: 14px;
  padding: 8px;
  border-radius: 6px;
  border: 1px solid #ccc;
  margin-bottom: 8px;
}
.comment-edit-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}
.comment-edit-confirm-btn,
.comment-edit-cancel-btn {
  padding: 6px 12px;
  font-size: 13px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
  transition: 0.2s;
}
.comment-edit-confirm-btn {
  background-color: #60a5fa;
  color: white;
}
.comment-edit-confirm-btn:hover {
  background-color: #3b82f6;
}
.comment-edit-cancel-btn {
  background-color: #e5e7eb;
  color: #374151;
}
.comment-edit-cancel-btn:hover {
  background-color: #d1d5db;
}
</style>
