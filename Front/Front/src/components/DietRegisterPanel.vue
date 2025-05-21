<template>
  <!-- 꼭 있어야 함 -->
<div class="backdrop" @click.self="emit('close')">

    <div class="panel">
      <div class="header">
        <h2>식단 등록</h2>
        <p>{{ date }} - {{ mealType }}</p>
        <button class="close-btn" @click="close">X</button>
      </div>

      <!-- 여기에 식품 검색창, 식단 추가 기능, 영양소 합산, 등록 버튼 등이 들어갈 예정 -->
      <div class="content">
        <p>식단 등록 폼이 여기에 들어갑니다.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  date: String,
  mealType: String,
})

const emit = defineEmits(['close'])

function close() {
  emit('close')
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
  width: 600px;
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
.content {
  flex-grow: 1;
  margin-top: 20px;
}
@keyframes slideIn {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}
</style>
