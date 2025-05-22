<!-- DietPreviewList.vue -->
<template>
  <div class="diet-preview-list" v-if="dietList.length > 0">
    <h4>등록 예정 식단</h4>
    <ul>
      <li v-for="(item, index) in dietList" :key="index">
        {{ item.name }} - ({{ item.kcal }}kcal)
        <button @click="emit('remove', index)">삭제</button>
      </li>
    </ul>
  </div>
</template>

<script setup>
const props = defineProps({
  dietList: Array
})
const emit = defineEmits(['remove'])
</script>

<style scoped>
.diet-preview-list {
  margin-bottom: 20px;
}
li {
  margin: 8px 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
button {
  padding: 4px 8px;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
}
</style>
