<template>
  <div class="backdrop" @click.self="close">
    <div class="panel">
      <div class="header">
        <h2>{{ date }} 식단 상세</h2>
        <button class="close-btn" @click="close">X</button>
      </div>

      <!-- 실제 데이터는 API나 props로 불러오게 구현 가능 -->
      <div class="meal-section">
        <p><strong>아침:</strong> 고구마, 달걀</p>
        <p><strong>점심:</strong> 닭가슴살, 밥</p>
        <p><strong>저녁:</strong> 샐러드</p>
        <p><strong>총 칼로리:</strong> 800 kcal</p>
      </div>

      <div class="footer">
        <div class="meal-buttons">
          <button @click="register('아침')">아침</button>
          <button @click="register('점심')">점심</button>
          <button @click="register('저녁')">저녁</button>
          <button @click="register('간식')">간식</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  date: String, // 예: '2025-06-01'
})
const emit = defineEmits(['close', 'openRegister'])

function close() {
  emit('close')
}
function register(mealType) {
  emit('openRegister', { date: props.date, mealType })
}
</script>

<style scoped>
.backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.3);
  z-index: 999;
}
.panel {
  position: absolute;
  top: 0;
  right: 0;
  width: 420px;
  height: 100%;
  background: #fff;
  padding: 20px;
  overflow-y: auto;
  animation: slideIn 0.3s ease-out forwards;
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.close-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
}
.meal-section {
  flex-grow: 1;
  margin-top: 20px;
}
.footer {
  text-align: center;
  margin-top: 20px;
}
.meal-buttons {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 10px;
}
.meal-buttons button {
  padding: 10px 20px;
  border-radius: 100px;
  border: none;
  color: white;
  font-weight: bold;
  cursor: pointer;
  width: 80px;
}
.meal-buttons button:nth-child(1) { background-color: #7fb77e; }
.meal-buttons button:nth-child(2) { background-color: #f8b400; }
.meal-buttons button:nth-child(3) { background-color: #6a67ce; }
.meal-buttons button:nth-child(4) { background-color: #ff8360; }
@keyframes slideIn {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}
</style>