<template>
  <!-- 등록페이지에서 검색어 입력시 연관검색어를 보여주는 컴포넌트입니다 -->
  <div class="search-result-list">
    <div v-if="foodList.length > 0">
      <div
        v-for="(food, index) in foodList"
        :key="index"
        class="food-item"
      >
        <div class="food-info">
          {{ food.name }} (100g) - {{ food.kcal }} kcal<br />
          탄: {{ food.carbo }} / 단: {{ food.protein }} / 지: {{ food.fat }}
        </div>
        <button @click="() => emit('selectFood', food)">선택</button>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({ foodList: Array })
const emit = defineEmits(['selectFood'])
</script>

<style scoped>
.food-item {
  border: 1px solid #ccc;
  padding: 10px;
  margin-bottom: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
button {
  padding: 6px 10px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
}
</style>
