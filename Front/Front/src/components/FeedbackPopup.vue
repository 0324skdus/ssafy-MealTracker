<template>
  <div class="popup-backdrop" @click.self="emit('close')">
    <div class="popup-panel">
      <h3 class="popup-title">GPT 식단 피드백</h3>

      <div class="popup-content">
        <p v-if="loading">피드백 생성 중입니다...</p>
        <p v-else-if="feedback" v-html="formattedFeedback"></p>
        <p v-else>피드백이 없습니다.</p>
      </div>

      <div class="popup-footer">
        <button class="refresh-btn" @click="emit('regenerate')">다시 피드백 받기</button>
        <button class="close-btn" @click="emit('close')">닫기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  feedback: String,
  loading: Boolean
})
const emit = defineEmits(['close', 'regenerate'])

const formattedFeedback = computed(() =>
  props.feedback?.replace(/\n/g, '<br />')
)
</script>

<style scoped>
.popup-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  z-index: 2000;
  display: flex;
  justify-content: center;
  align-items: center;
}

.popup-panel {
  background: white;
  width: 500px;
  max-height: 80%;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  overflow-y: auto;
}

.popup-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 16px;
}

.popup-content {
  font-size: 14px;
  line-height: 1.6;
  white-space: normal;
  word-break: break-word;
  margin-bottom: 24px;
}

.popup-footer {
  display: flex;
  justify-content: space-between;
}

.close-btn,
.refresh-btn {
  padding: 8px 16px;
  border: none;
  font-weight: bold;
  border-radius: 6px;
  cursor: pointer;
}

.close-btn {
  background-color: #ccc;
  color: black;
}

.refresh-btn {
  background-color: #4c8bf5;
  color: white;
}

.refresh-btn:hover {
  background-color: #2e6ff2;
}
</style>
