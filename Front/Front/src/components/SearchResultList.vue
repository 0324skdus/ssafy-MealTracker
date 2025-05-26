<template>
  <!-- 검색 결과 목록 -->
  <div class="search-result-list">
    <div v-if="paginatedList.length > 0">
      <div
        v-for="(food, index) in paginatedList" 
        :key="index"
        class="food-item"
      >
        <div class="food-info">
          {{ food.foodName }} (100g) - {{ food.kcal }} kcal<br />
          탄: {{ food.carbo }} / 단: {{ food.protein }} / 지: {{ food.fat }}
        </div>
        <button @click="() => emit('selectFood', food)">✅</button>
      </div>

      <!-- 페이지네이션 버튼 UI -->
      <div class="pagination">
        <button @click="prevPage" :disabled="currentPage === 1"><</button>
        <span> {{ currentPage }} / {{ totalPages }} </span>
        <button @click="nextPage" :disabled="currentPage === totalPages">></button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'

const props = defineProps({ foodList: Array })
const emit = defineEmits(['selectFood'])

const itemsPerPage = 5 // 페이지당 표시할 항목 수
const currentPage = ref(1) // 현재 페이지 번호

// 전체 페이지 수 계산
const totalPages = computed(() => Math.ceil(props.foodList.length / itemsPerPage))

// 현재 페이지에 해당하는 항목 추출
const paginatedList = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage
  return props.foodList.slice(start, start + itemsPerPage)
})

// 이전 페이지로 이동
const prevPage = () => {
  if (currentPage.value > 1) currentPage.value--
}

// 다음 페이지로 이동
const nextPage = () => {
  if (currentPage.value < totalPages.value) currentPage.value++
}

// 검색 결과가 바뀌면 첫 페이지로 초기화
watch(() => props.foodList, () => {
  currentPage.value = 1
})
</script>

<style scoped>
.food-item {
  border: 1px solid #ccc;
  padding: 10px;
  margin-bottom: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 8px;
}
button {
  padding: 6px 10px;
  color: rgb(0, 0, 0);
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 20px;
}
.pagination {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 15px;
  align-items: center;
}
</style>
