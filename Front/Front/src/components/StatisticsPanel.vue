<template>
  <div class="statistics-panel">
    <!-- 상단 버튼 -->
    <div class="controls">
      <div class="period-buttons">
        <button
          v-for="option in periodOptions"
          :key="option.value"
          :class="{ active: selectedPeriod === option.value }"
          @click="changePeriod(option.value)"
        >
          {{ option.label }}
        </button>
      </div>
      <div class="view-buttons">
        <button :class="{ active: viewMode === 'average' }" @click="viewMode = 'average'">평균치 보기</button>
        <button :class="{ active: viewMode === 'graph' }" @click="viewMode = 'graph'">그래프 보기</button>
      </div>
    </div>

    <!-- 평균치 보기 -->
    <div v-if="viewMode === 'average'" class="average-view">
      <div class="nutrient" v-for="(value, key) in nutrition" :key="key">
        <span class="label">{{ nutrientLabels[key] }}:</span>
        <div class="bar-wrapper">
          <div
            class="bar"
            :style="{ width: getBarWidth(key, value) + '%' }"
          ></div>
          <span class="value">{{ value.toFixed(1) }}</span>
        </div>
      </div>
      <div class="score">
        평균 점수: <strong>{{ averageScore.toFixed(1) }}</strong>점
      </div>
    </div>

    <!-- 그래프 보기 (추후 구현) -->
    <div v-else>
      <p>그래프는 추후 구현 예정입니다.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const userId = localStorage.getItem('userId')
const token = localStorage.getItem('accessToken')

const selectedPeriod = ref('1W')
const viewMode = ref('average')

const periodOptions = [
  { value: '1W', label: '1주일' },
  { value: '1M', label: '1달' },
  { value: '3M', label: '3달' },
  { value: '6M', label: '6달' },
  { value: '1Y', label: '1년' }
]

const nutrition = ref({
  kcal: 0,
  carbo: 0,
  protein: 0,
  fat: 0,
  fiber: 0,
  sugar: 0,
  transfat: 0
})

const targetNutrition = ref({
  kcal: 2000,
  carbo: 250,
  protein: 100,
  fat: 60,
  fiber: 25,
  sugar: 50,
  transfat: 2
})

const averageScore = ref(0)

const nutrientLabels = {
  kcal: '칼로리 (kcal)',
  carb: '탄수화물 (g)',
  protein: '단백질 (g)',
  fat: '지방 (g)',
  fiber: '식이섬유 (g)',
  sugar: '당류 (g)',
  transfat: '트랜스지방 (g)'
}

const fetchData = async () => {
  try {
    const [nutritionRes, scoreRes, targetRes] = await Promise.all([
      axios.get(`/api/nutrition/average`, {
        params: { userId, period: selectedPeriod.value },
        headers: { Authorization: `Bearer ${token}` }
      }),
      axios.get(`/api/score/${userId}/average`, {
        params: { period: selectedPeriod.value },
        headers: { Authorization: `Bearer ${token}` }
      }),
      axios.get(`/api/nutrition/target`, {
        params: { userId },
        headers: { Authorization: `Bearer ${token}` }
      })
    ])
    nutrition.value = nutritionRes.data
    averageScore.value = scoreRes.data
    targetNutrition.value = targetRes.data
  } catch (err) {
    console.error('식단 통계 조회 실패:', err)
  }
}

const changePeriod = (period) => {
  selectedPeriod.value = period
  fetchData()
}

const getBarWidth = (key, value) => {
  const target = targetNutrition.value[key]
  if (!target || target === 0) return 0
  return Math.min((value / target) * 100, 100)
}

onMounted(fetchData)
</script>

<style scoped>
.statistics-panel {
  background-color: #f9f9f9;
  padding: 24px;
  border-radius: 8px;
}

.controls {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.period-buttons button,
.view-buttons button {
  margin-right: 10px;
  padding: 6px 12px;
  border: none;
  border-radius: 6px;
  background-color: #ddd;
  cursor: pointer;
}

button.active {
  background-color: #4caf50;
  color: white;
}

.average-view {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.nutrient {
  display: flex;
  flex-direction: column;
}

.label {
  font-weight: bold;
  margin-bottom: 4px;
}

.bar-wrapper {
  background-color: #eee;
  border-radius: 4px;
  position: relative;
  height: 24px;
}

.bar {
  background-color: #4caf50;
  height: 100%;
  border-radius: 4px;
  transition: width 0.3s ease;
}

.value {
  position: absolute;
  top: 0;
  right: 8px;
  font-size: 14px;
  line-height: 24px;
}

.score {
  margin-top: 20px;
  font-size: 18px;
  font-weight: bold;
}
</style>
