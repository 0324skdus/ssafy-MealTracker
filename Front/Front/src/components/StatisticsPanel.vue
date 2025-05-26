<template>
  <div class="statistics-panel">
    <!-- 상단: 기간 선택 버튼 -->
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
    </div>

    <!-- 본문: 그래프 + 평균치 동시 노출 -->
    <div class="stat-body">
      <!-- 그래프 영역 -->
      <div class="chart-half">
        <Line :data="chartData" :options="chartOptions" :height="220" />
      </div>

      <!-- 평균치 막대그래프 영역 -->
      <div class="average-half">
        <div class="nutrient" v-for="(value, key) in nutrition" :key="key">
          <span class="label">{{ nutrientLabels[key] }}:</span>
          <div class="bar-wrapper">
            <div class="bar" :style="{ width: getBarWidth(key, value) + '%' }" />
            <span class="value">{{ value.toFixed(1) }}</span>
          </div>
        </div>
        <div class="score">
          평균 점수: <strong>{{ averageScore.toFixed(1) }}</strong>점
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import { Line } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement
} from 'chart.js'

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement
)

const userId = localStorage.getItem('userId')
const token = localStorage.getItem('accessToken')

const selectedPeriod = ref('1W')

const periodOptions = [
  { value: '1W', label: '1주일' },
  { value: '1M', label: '1달' },
  { value: '3M', label: '3달' },
  { value: '6M', label: '6달' },
  { value: '1Y', label: '1년' }
]

const nutrition = ref({
  kcal: 0, carb: 0, protein: 0, fat: 0, fiber: 0, sugar: 0, transfat: 0
})
const targetNutrition = ref({
  kcal: 0, carb: 0, protein: 0, fat: 0, fiber: 0, sugar: 0, transfat: 0
})
const averageScore = ref(0)
const scoreHistory = ref([])

const nutrientLabels = {
  kcal: '칼로리 (kcal)',
  carb: '탄수화물 (g)',
  protein: '단백질 (g)',
  fat: '지방 (g)',
  fiber: '식이섬유 (g)',
  sugar: '당류 (g)',
  transfat: '트랜스지방 (g)'
}

const fetchAllData = async () => {
  try {
    const [nutritionRes, scoreRes, targetRes, historyRes] = await Promise.all([
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
      }),
      axios.get(`/api/score/${userId}/history`, {
        params: { period: selectedPeriod.value },
        headers: { Authorization: `Bearer ${token}` }
      })
    ])
    nutrition.value = nutritionRes.data
    averageScore.value = scoreRes.data
    targetNutrition.value = targetRes.data
    scoreHistory.value = historyRes.data
  } catch (err) {
    console.error('데이터 조회 실패:', err)
  }
}

const changePeriod = (period) => {
  selectedPeriod.value = period
  fetchAllData()
}

const getBarWidth = (key, value) => {
  const target = targetNutrition.value[key]
  if (!target || target === 0) return 0
  return Math.min((value / target) * 100, 100)
}

const chartData = computed(() => ({
  labels: scoreHistory.value.map(item => item.date),
  datasets: [{
    label: '일별 식단 점수',
    data: scoreHistory.value.map(item => item.score),
    borderColor: '#4caf50',
    backgroundColor: 'rgba(76, 175, 80, 0.2)',
    tension: 0.3,
    fill: true,
  }]
}))

const chartOptions = {
  responsive: true,
  plugins: {
    legend: { display: false },
    tooltip: { enabled: true }
  },
  scales: {
    y: {
      beginAtZero: true,
      max: 100
    }
  }
}

onMounted(fetchAllData)
</script>

<style scoped>
.statistics-panel {
  background-color: #ffffff;   /* ← 회색 제거 */
  padding: 16px;               /* ← 여백 줄이기 */
  border-radius: 8px;
}
.controls {
  display: flex;
  justify-content: flex-start;
  margin: 50px 0 20px;  /* ← 위쪽 여백 추가 */
}

.period-buttons button {
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

/* 본문 가로배치 */
.stat-body {
  display: flex;
  gap: 24px;
  align-items: stretch; /* ← 높이 맞추기 핵심 */
}

.chart-half,
.average-half {
  flex: 1;
}

/* 평균 막대그래프 스타일 */
.average-half {
  display: flex;
  flex-direction: column;
  justify-content: space-between; /* 항목이 전체 영역 안에서 분산되도록 */
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
