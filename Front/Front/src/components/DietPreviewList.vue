<template>
  <div class="pending-diet-list">
    <!-- 등록페이지에서 등록예정식단을 보여주는 구역입니다 -->
    <h4>등록 예정 식단</h4>
    <ul>
      <li v-for="(item, index) in dietList" :key="index">
        {{ item.foodName }} - ({{ item.kcal }}kcal)
        <button @click="removeLocal(index)">삭제</button>
      </li>
    </ul>
  </div>
</template>

<script setup>
const props = defineProps({
  dietList: Array
})
const emit = defineEmits(['remove'])

const removeLocal = (index) => {
  emit('remove', index)
}
</script>

<style scoped>
.pending-diet-list ul {
  list-style: none;
  padding: 0;
}
.pending-diet-list li {
  margin-bottom: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
button {
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 4px 8px;
  cursor: pointer;
}
</style>
