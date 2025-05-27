<template>
  <div class="popup-backdrop" @click.self="emit('close')">
    <div class="popup-panel">
      <h3 class="popup-title">식단 피드백</h3>

      <div class="popup-content">
        <p v-if="loading">피드백 생성 중입니다...</p>
        <p v-else-if="feedback" v-html="formattedFeedback"></p>
        <p v-else>피드백이 없습니다.</p>
      </div>

      <div class="popup-footer">
        <button class="refresh-btn" @click="emit('regenerate')">피드백 업데이트</button>
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

function autoLinkFeedback(feedbackText) {
  if (!feedbackText) return ''

  let result = feedbackText.replace(/\n/g, '<br />')

  // 추천 식품 추출 패턴 (예: "추천드립니다: 닭가슴살, 바나나, ...")
  const foodPattern = /(?:추천(?:드립니다|합니다|해요)?|드시는 것을 추천|드셔보세요|추가하시면 좋습니다)[^\n]*?([가-힣a-zA-Z0-9\s,·•ㆍ\/]+)[\.\n]/gi

  const matches = [...feedbackText.matchAll(foodPattern)]

  for (const match of matches) {
    const foods = match[1]
      .split(/,|·|•|ㆍ|\/|or|그리고|또는/)
      .map(s => s.trim())
      .filter(s => s.length >= 2 && s.length <= 20 && /^[가-힣a-zA-Z0-9\s]+$/.test(s))

    for (const food of foods) {
      const url = `https://www.coupang.com/np/search?q=${encodeURIComponent(food)}`
      const linked = `<a href="${url}" target="_blank">${food}</a>`
      const plainPattern = new RegExp(food.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&'), 'g')
      result = result.replace(plainPattern, linked)
    }
  }

  // 일반적인 URL도 자동 링크 처리
  result = result.replace(/https?:\/\/[^\s<>()]+/g, (url) => {
    return `<a href="${url}" target="_blank">${url}</a>`
  })

  return result
}

const formattedFeedback = computed(() => autoLinkFeedback(props.feedback))
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

.popup-content a {
  color: #1a0dab;
  text-decoration: underline;
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
