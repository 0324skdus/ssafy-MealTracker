<template>
  <div class="backdrop" @click.self="emit('close')">
    <div class="panel">
      <div class="header">
        <div class="left-buttons">
          <button class="back-btn" @click="emit('backToDetail')"><</button>
        </div>
        <h2>
          📅{{ date }} {{ mealType }}
        </h2>
        <button class="close-btn" @click="close">X</button>
      </div>

      <div class="diet-register-panel">
        <!-- 검색 -->
        <div class="search-bar">
          <input v-model="keyword" placeholder="식품명을 입력하세요" @keyup.enter="onSearch" />
          <button @click="onSearch">검색</button>
          <button class="manual-button" @click="showManualForm = !showManualForm">직접 입력</button>
          <ul v-if="autoCompleteResults.length" class="autocomplete-list">
            <li v-for="item in autoCompleteResults" :key="item.id" @click="handleSelectFood(item)">
              {{ item.foodName }}
            </li>
          </ul>
        </div>

        <!-- 직접 입력 폼 -->
       <div v-if="showManualForm" class="manual-form">
  <label>음식명</label>
  <input v-model="manualForm.name" placeholder="예: 삶은계란" />

  <label>칼로리 (kcal)</label>
  <input v-model.number="manualForm.kcal" />

  <label>탄수화물 (g)</label>
  <input v-model.number="manualForm.carbo" />

  <label>단백질 (g)</label>
  <input v-model.number="manualForm.protein" />

  <label>지방 (g)</label>
  <input v-model.number="manualForm.fat" />

  <label>당류 (g)</label>
  <input v-model.number="manualForm.sugar" />

  <label>식이섬유 (g)</label>
  <input v-model.number="manualForm.fiber" />

  <label>트랜스지방 (g)</label>
  <input v-model.number="manualForm.transfat" />

  <label>포화지방 (g)</label>
  <input v-model.number="manualForm.saturatedFat" />

  <button class="manual-add-button" @click="handleAddManualFood">추가</button>
</div>
<br>

        <!-- 검색 결과 -->
        <SearchResultList :foodList="foodList" @selectFood="handleSelectFood" />
<br>
        <!-- 선택된 식품 정보 -->
        <SelectedFoodInfo v-if="selectedFood" :food="selectedFood" @addDiet="handleAddDiet" />

        <!-- 등록 예정 식단 목록 -->
        <div class="section-card pending">
          <DietPreviewList :dietList="newDietList" @remove="(i) => newDietList.splice(i, 1)" />
          <div class="register-button" v-if="newDietList.length > 0">
            <button @click="handleRegisterDiet">등록 하기</button>
          </div>
        </div>

        <!-- 현재 등록된 식단 목록 -->
        <div class="section-card registered">
          <RegisteredDietList :dietList="dietList" @deleted="(i) => dietList.splice(i, 1)" />
        </div>

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
const dietList = ref([])
const newDietList = ref([])

const showManualForm = ref(false)
const manualForm = ref({
  name: '',
  kcal: 0,
  carbo: 0,
  protein: 0,
  fat: 0,
  sugar: 0,
  fiber: 0,
  transfat: 0,
  saturatedFat: 0
})

const mealTypeMap = {
  아침: 'breakfast',
  점심: 'lunch',
  저녁: 'dinner',
  간식: 'snack'
}

const onSearch = () => {
  if (keyword.value.trim()) handleSearch(keyword.value.trim())
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

const handleSelectFood = (food) => {
  selectedFood.value = food
  autoCompleteResults.value = []
}

const handleAddDiet = (dietItem) => {
  newDietList.value.push(dietItem)
}

const handleAddManualFood = () => {
  if (!manualForm.value.name || manualForm.value.kcal <= 0) {
    alert('음식명과 칼로리를 입력하세요.')
    return
  }
  newDietList.value.push({
    ...manualForm.value,
    foodName: manualForm.value.name
  })
  manualForm.value = {
    name: '',
    kcal: 0,
    carbo: 0,
    protein: 0,
    fat: 0,
    sugar: 0,
    fiber: 0,
    transfat: 0,
    saturatedFat: 0
  }
  showManualForm.value = false
}

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
  background: rgba(0, 0, 0, 0.4);
  z-index: 999;
}
.panel {
  position: absolute;
  top: 0;
  right: 0;
  width: 600px;
  height: 100%;
  background: #ffffff; /* 크림빛 연두 배경 */
  padding: 24px;
  overflow-y: auto;
  animation: slideIn 0.3s ease-out forwards;
  box-shadow: -4px 0 12px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  border-radius: 12px 0 0 12px;
  font-weight: bold;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.header h2 {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}
.back-btn,
.close-btn {
  background: none;
  border: none;
  font-size: 20px;
  font-weight: 700;
  color: #444;
  cursor: pointer;
  transition: transform 0.2s ease, color 0.2s;
}
.search-bar {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
  position: relative;
}
.search-bar input {
  flex: 1;
  padding: 10px;
  border: 1px solid #b0bec5;
  border-radius: 8px;
  font-size: 14px;
}
.search-bar button {
  padding: 8px 12px;
  font-size: 13px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  color: white;
}
.search-bar button:first-of-type {
  background-color: #4c8bf5; 
}
.manual-button {
  background-color: #e76f51; 
}
.manual-form {
  
  border-radius: 10px;
  padding: 16px;
  margin-bottom: 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.manual-form label {
  font-weight: 600;
  font-size: 13px;
  color: #555;
}
.manual-form input {
  border: 1px solid #ccc;
  padding: 8px;
  border-radius: 6px;
  
}
.manual-add-button {
  background-color: #ff9800; /* 포인트 주황 */
  color: white;
  margin-top: 20px;
  border-radius: 8px;
}
.section-card {
  border-radius: 10px;
  padding: 14px 18px;
  margin-bottom: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
  font-size: 14px;
}
.section-card.pending {
  background-color: #d6d7da; 
}
.section-card.registered {
  background-color: #c4e3f7; 
}
.autocomplete-list {
  position: absolute;
  top: 44px;
  left: 0;
  background: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  width: 100%;
  max-height: 180px;
  overflow-y: auto;
  z-index: 10;
  font-size: 13px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}
.autocomplete-list li {
  padding: 10px;
  cursor: pointer;
  transition: background 0.2s;
}
.autocomplete-list li:hover {
  background: #f1f8e9;
}
.register-button {
  display: flex;
  justify-content: center;
}
.register-button button {
  background-color: #4c8bf5; 
  border: none;
  padding: 10px 20px;
  font-size: 14px;
  border-radius: 8px;
  color: white;
  cursor: pointer;
  margin-top: 12px;
  font-weight: 100;
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


