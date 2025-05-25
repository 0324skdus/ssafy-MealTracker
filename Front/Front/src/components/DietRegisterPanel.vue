<template>
  <div class="backdrop" @click.self="emit('close')">
    <div class="panel">
      <div class="header">
        <div class="left-buttons">
          <button class="back-btn" @click="emit('backToDetail')">〈</button>
        </div>
        <!-- 식단등록 페이지입니다 -->
        <h2>식단 등록</h2>
        <p>{{ date }} - {{ mealType }}</p>
        <button class="close-btn" @click="close">X</button>
      </div>

      <div class="diet-register-panel">
        <!-- 검색 -->
        <div class="search-bar">
          <input v-model="keyword" placeholder="식품명을 입력하세요" @keyup.enter="onSearch" />
          <button @click="onSearch">검색</button>
          <ul v-if="autoCompleteResults.length" class="autocomplete-list">
            <li
              v-for="item in autoCompleteResults"
              :key="item.id"
              @click="handleSelectFood(item)">
              {{ item.foodName }}
            </li>
          </ul>
        </div>

        <!-- 검색 결과 -->
        <SearchResultList :foodList="foodList" @selectFood="handleSelectFood" />

        <!-- 선택된 식품 정보 -->
        <SelectedFoodInfo v-if="selectedFood" :food="selectedFood" @addDiet="handleAddDiet" />

        <!-- 등록 예정 식단 목록 (로컬) -->
        <div class="section-card pending">
        <DietPreviewList :dietList="newDietList" @remove="(i) => newDietList.splice(i, 1)" />
          <!-- 등록 버튼 -->
          <div class="register-button" v-if="newDietList.length > 0">
            <button @click="handleRegisterDiet">등록 완료</button>
          </div>
        </div>
        
          
        <!-- 현재 서버 등록된 식단 목록 -->
        <div class="section-card registered">
           <RegisteredDietList :dietList="dietList" @deleted="(i) => dietList.splice(i, 1)" />
         </div>

        <!-- 등록된 식단이 없을 때 -->
        <div v-if="dietList.length === 0 && !selectedFood">
          <p style="margin-top: 20px; color: #888;">등록된 식단이 없습니다.</p>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'

import SearchResultList from './SearchResultList.vue'
import SelectedFoodInfo from './SelectedFoodInfo.vue'
import RegisteredDietList from './RegisteredDietList.vue'
import DietPreviewList from './DietPreviewList.vue'

const props = defineProps({
  date: String,
  mealType: String
})

const emit = defineEmits(['close', 'backToDetail', 'refreshMonthlyData'])

const keyword = ref('')
const foodList = ref([])
const autoCompleteResults = ref([])
const selectedFood = ref(null)
const dietList = ref([])        // 기존 등록된 식단
const newDietList = ref([])     // 새로 추가한 등록 예정 식단

const mealTypeMap = {
  아침: 'breakfast',
  점심: 'lunch',
  저녁: 'dinner',
  간식: 'snack'
}

// 검색
const onSearch = () => {
  if (keyword.value.trim()) {
    handleSearch(keyword.value.trim())
  }
}

const handleSearch = async (keyword) => {
  try {
    const res = await axios.get('/api/food/search', { params: { keyword } })
    foodList.value = res.data
  } catch (err) {
    console.error('음식 검색 실패:', err)
    foodList.value = []
  }
}

// 자동완성
const handleAutoComplete = async () => {
  const trimmed = keyword.value.trim()
  if (!trimmed) return (autoCompleteResults.value = [])
  try {
    const res = await axios.get('/api/food/search', { params: { keyword: trimmed } })
    autoCompleteResults.value = res.data
  } catch (err) {
    console.error('자동완성 실패:', err)
    autoCompleteResults.value = []
  }
}
watch(keyword, handleAutoComplete)

// 선택 식품
const handleSelectFood = (food) => {
  selectedFood.value = food
  autoCompleteResults.value = []
}

// 새 식단 추가
const handleAddDiet = (dietItem) => {
  newDietList.value.push(dietItem)
}

// 등록
const handleRegisterDiet = async () => {
  const token = localStorage.getItem('accessToken')
  const userId = localStorage.getItem('userId')
  try {
    for (const item of newDietList.value) {
      await axios.post('/api/diet/by-food', {
        userId,
        date: props.date,
        mealType: mealTypeMap[props.mealType],
        foodId: item.id,
        foodName: item.name || item.foodName,
        kcal: item.kcal,
        carbo: item.carbo,
        protein: item.protein,
        fat: item.fat,
        sugar: item.sugar || 0,
        fiber: item.fiber || 0,
        transfat: item.transfat || 0,
        saturatedFat: item.saturatedFat || 0,
        score: item.score || 0,
        weight: item.weight || 100
      }, {
        headers: { Authorization: `Bearer ${token}` }
      })
    }
    alert('식단이 등록되었습니다!')
    emit('refreshMonthlyData')
    emit('close')
  } catch (err) {
    console.error('식단 등록 실패:', err)
    alert('등록 중 오류가 발생했습니다.')
  }
}

// 기존 식단 조회
const fetchDietList = async () => {
  const token = localStorage.getItem('accessToken')
  const userId = localStorage.getItem('userId')
  if (!userId || !props.date || !props.mealType || !token) return
  try {
    const res = await axios.get(`/api/diet/user/${userId}/date`, {
      params: { date: props.date },
      headers: { Authorization: `Bearer ${token}` }
    })
    const mealKey = mealTypeMap[props.mealType]
    dietList.value = res.data.filter(d => d.mealType === mealKey)
    emit('refreshMonthlyData')
  } catch (err) {
    console.error('식단 재조회 실패', err)
  }
}

const close = () => emit('close')

onMounted(fetchDietList)
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
.register-button {
  text-align: center;
  margin-top: 20px;
}
.search-bar {
  margin-bottom: 16px;
  display: flex;
  gap: 8px;
  position: relative;
}
/* 등록예정/등록식단 구분섹션 */
.section-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 12px 16px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}
.section-card.pending {
  background-color: #fff8e1; /* 연노랑 */
}
.section-card.registered {
  background-color: #e3f2fd; /* 연파랑 */
}
/* 자동완성 css */
.autocomplete-list {
  position: absolute;
  top: 40px;
  left: 0;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 100%;
  max-height: 200px;
  overflow-y: auto;
  z-index: 10;
}
.autocomplete-list li {
  padding: 8px;
  cursor: pointer;
}
.autocomplete-list li:hover {
  background-color: #eee;
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
