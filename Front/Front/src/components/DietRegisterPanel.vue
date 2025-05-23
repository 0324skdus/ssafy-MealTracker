<template>
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

        <!-- 3. 선택된 식품 정보 -->
        <SelectedFoodInfo
          v-if="selectedFood"
          :food="selectedFood"
          @addDiet="handleAddDiet"
        />

        <!-- 4. 미리보기 리스트 -->
        <DietPreviewList
          :dietList="dietList"
          @remove="handleRemoveDiet"
          @deleted="fetchDietList"
        />

        <!-- 등록된 식단이 없을 때 -->
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
import { ref, onMounted } from 'vue'
import axios from 'axios'

import SearchResultList from './SearchResultList.vue'
import SelectedFoodInfo from './SelectedFoodInfo.vue'
import DietPreviewList from './DietPreviewList.vue'

const props = defineProps({
  date: String,
  mealType: String,
  existingDiet: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['close', 'backToDetail'])

const foodList = ref([])
const selectedFood = ref(null)
const dietList = ref([])
const keyword = ref('')

const mealTypeMap = {
  아침: 'breakfast',
  점심: 'lunch',
  저녁: 'dinner',
  간식: 'snack'
}

const onSearch = () => {
  if (keyword.value.trim()) {
    handleSearch(keyword.value.trim())
  }
}

const handleSearch = async (keyword) => {
  console.log('검색어:', keyword)
  try {
    const res = await axios.get('/api/food/search', {
      params: { keyword }
    })
    foodList.value = res.data
  } catch (err) {
    console.error('음식 검색 실패:', err)
    foodList.value = []
  }
}

const handleSelectFood = (food) => {
  selectedFood.value = food
}

const handleAddDiet = (dietItem) => {
  dietList.value.push(dietItem)
}

const handleRemoveDiet = (index) => {
  dietList.value.splice(index, 1)
}

const handleRegisterDiet = () => {
  console.log('등록 요청:', dietList.value)
}

const close = () => {
  emit('close')
}

const fetchDietList = async () => {
  const token = localStorage.getItem('accessToken')
  const userId = localStorage.getItem('userId')

  if (!userId || !props.date || !props.mealType || !token) return

  try {
    const res = await axios.get(`/api/diet/user/${userId}/date`, {
      params: { date: props.date },
      headers: {
        Authorization: `Bearer ${token}`
      }
    })

    const mealKey = mealTypeMap[props.mealType]
    dietList.value = res.data.filter(d => d.mealType === mealKey)
  } catch (err) {
    console.error('식단 재조회 실패', err)
  }
}

onMounted(() => {
  fetchDietList()
})
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
