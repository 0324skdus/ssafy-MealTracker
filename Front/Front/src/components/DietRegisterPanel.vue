<template>
  <!-- 꼭 있어야 함 -->
<div class="backdrop" @click.self="emit('close')">

    <div class="panel">
      <div class="header">
        <div class="left-buttons">
          <button class="back-btn" @click="emit('backToDetail')">〈</button>
        </div>
        <h2>식단 등록</h2>
        <p>{{ date }} - {{ mealType }}</p>
        <button class="close-btn" @click="close">X</button>
      </div>

      <!-- 여기에 식품 검색창, 식단 추가 기능, 영양소 합산, 등록 버튼 등이 들어갈 예정 -->
        <div class="diet-register-panel">
    <!-- 1. 검색 영역 -->
    <div class="search-bar">
    <input v-model="keyword" placeholder="식품명을 입력하세요" @keyup.enter="onSearch" />
    <button @click="onSearch">검색</button>
  </div>

    <!-- 2. 검색 결과 리스트 -->
    <SearchResultList
      :foodList="foodList"
      @selectFood="handleSelectFood"
    />

    <!-- 3. 선택된 식품 정보 + 섭취량 입력 -->
    <SelectedFoodInfo
      v-if="selectedFood"
      :food="selectedFood"
      @addDiet="handleAddDiet"
    />

    <!-- 4. 미리보기 리스트 -->
    <DietPreviewList
      :dietList="dietList"
      @remove="handleRemoveDiet"
    />
    <!-- 식단이 전혀 없을 경우 안내 문구 -->
<div v-if="dietList.length === 0 && !selectedFood">
  <p style="margin-top: 20px; color: #888;">등록된 식단이 없습니다.</p>
</div>

    <!-- 5. 최종 등록 버튼 -->
    <div class="register-button" v-if="dietList.length > 0">
      <button @click="handleRegisterDiet">등록 완료</button>
    </div>
  </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  date: String,
  mealType: String,
  existingDiet: {
    type: Array,
    default: () => []
  }
})
onMounted(() => {
  dietList.value = props.existingDiet.map(item => ({
    ...item,
    name: item.foodName || item.name,
    weight: item.weight || 0
  }))
})

import { ref, onMounted } from 'vue'

// 임포트할 컴포넌트 껍데기들
import SearchResultList from './SearchResultList.vue'
import SelectedFoodInfo from './SelectedFoodInfo.vue'
import DietPreviewList from './DietPreviewList.vue'
// 상태
const emit = defineEmits(['close','backToDetail'])
const foodList = ref([]) // 검색 결과 식품 리스트
const selectedFood = ref(null) // 선택된 식품
const dietList = ref([]) // 현재 등록할 식단 목록
const keyword = ref('')


function onSearch() {
  if (keyword.value.trim()) {
    handleSearch(keyword.value.trim())
  }
}

function handleSearch(keyword) {
  //채울부분분
  console.log('검색어:', keyword)

  // 더미
  foodList.value = [
    {
      id: 1,
      name: '고구마',
      kcal: 130,
      carbo: 30,
      protein: 2,
      fat: 0.2
    },
    {
      id: 2,
      name: '닭가슴살',
      kcal: 110,
      carbo: 1,
      protein: 23,
      fat: 1.5
    }
  ]
}

function handleSelectFood(food) {
  selectedFood.value = food
}

function handleAddDiet(dietItem) {
  dietList.value.push(dietItem)
}

function handleRemoveDiet(index) {
  dietList.value.splice(index, 1)
}

function handleRegisterDiet() {
  //채울부분분
  console.log('등록 요청:', dietList.value)
}

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
.register-button {
  text-align: center;
  margin-top: 20px;
}
.search-bar {
  margin-bottom: 16px;
  display: flex;
  gap: 8px;
}
input {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
button {
  padding: 8px 12px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
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
