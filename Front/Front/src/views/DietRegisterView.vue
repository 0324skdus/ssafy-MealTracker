<template>
  <div v-if="displayItems.length > 0" class="register-wrapper">
    <div class="register-container">
      <div class="logo-box">
        <img src="/logo.png" alt="Meal Tracker Logo" class="logo" />
      </div>

      <div class="date-title">{{ date }} 식단 등록</div>

      <div class="kcal-bar-wrapper">
        <div class="kcal-bar">
          <div class="kcal-fill" :style="{ width: computePercent(displayItems[0]) + '%' }"></div>
        </div>
        <div class="kcal-text">
          {{ displayItems[0].current }} / {{ displayItems[0].goal }} kcal
        </div>
      </div>

      <div class="nutrient-progress-wrapper">
        <div v-for="item in displayItems.slice(1)" :key="item.label" class="nutrient-progress-box">
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: computePercent(item) + '%' }"></div>
          </div>
          <div class="progress-text">
            {{ item.label }} {{ item.current }} / {{ item.goal }} {{ item.unit }}
          </div>
        </div>
      </div>

      <div class="meal-buttons">
        <button
          v-for="type in ['아침', '점심', '저녁', '간식']"
          :key="type"
          class="meal-button"
          @click="goToDetail(type)"
        >
          {{ type }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const date = route.query.date

const nutritionGoal = ref({})
const nutritionCurrent = ref({})
const displayItems = ref([])

function computePercent(item) {
  const ratio = (item.current / item.goal) * 100
  return ratio > 100 ? 100 : Math.round(ratio)
}

function goToDetail(mealType) {
  router.push(`/register-diet-detail?date=${date}&mealType=${mealType}`)
}

onMounted(async () => {
  const userId = localStorage.getItem('userId')
  const token = localStorage.getItem('accessToken')
  const date = route.query.date
  
 console.log("accessToken in localStorage:", token)       // ✅ 확인 1
  const headers = {
    Authorization: `Bearer ${token}`
  }
  console.log("axios headers:", headers)                   // ✅ 확인 2

  if (!token) {
    console.warn("No token in localStorage, redirecting to login")
    router.push('/login')
    return
  }

  

  try {
    const res1 = await axios.get(`/api/user/me/daily-target`, { headers })
    const res2 = await axios.get(`/api/diet/sum?userId=${userId}&date=${date}`, { headers })

    nutritionGoal.value = res1.data ?? {}
    nutritionCurrent.value = res2.data ?? {}

    displayItems.value = [
      { label: '칼로리', current: nutritionCurrent.value.kcal ?? 0, goal: nutritionGoal.value.kcal ?? 0, unit: 'kcal' },
      { label: '탄수화물', current: nutritionCurrent.value.carbo ?? 0, goal: nutritionGoal.value.carbo ?? 0, unit: 'g' },
      { label: '단백질', current: nutritionCurrent.value.protein ?? 0, goal: nutritionGoal.value.protein ?? 0, unit: 'g' },
      { label: '지방', current: nutritionCurrent.value.fat ?? 0, goal: nutritionGoal.value.fat ?? 0, unit: 'g' },
      { label: '식이섬유', current: nutritionCurrent.value.fiber ?? 0, goal: nutritionGoal.value.fiber ?? 0, unit: 'g' },
      { label: '당류', current: nutritionCurrent.value.sugar ?? 0, goal: nutritionGoal.value.sugar ?? 0, unit: 'g' },
    ]
  } catch (e) {
    console.error("API 요청 실패", e)
  }
})
</script>

<style scoped>
.register-wrapper {
  width: 100%;
  min-height: 100vh;
  background-color: white;
  display: flex;
  justify-content: center;
  padding: 2rem 0;
}

.register-container {
  width: 100%;
  max-width: 26rem;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.logo-box {
  display: flex;
  justify-content: center;
}

.logo {
  width: 8rem;
}

.date-title {
  font-size: 1.2rem;
  font-weight: bold;
  text-align: center;
}

.kcal-bar-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.25rem;
}

.kcal-bar {
  width: 100%;
  height: 1rem;
  background-color: #f3f4f6;
  border-radius: 0.375rem;
  overflow: hidden;
}

.kcal-fill {
  height: 100%;
  background-color: #a3e635;
  transition: width 0.3s ease;
}

.kcal-text {
  font-size: 0.9rem;
  font-weight: bold;
}

.nutrient-progress-wrapper {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.nutrient-progress-box {
  background-color: #fef9c3;
  border-radius: 0.375rem;
  padding: 0.5rem;
}

.progress-bar {
  width: 100%;
  height: 0.75rem;
  background-color: #e5e7eb;
  border-radius: 0.375rem;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background-color: #a3e635;
  transition: width 0.3s ease;
}

.progress-text {
  margin-top: 0.25rem;
  font-size: 0.85rem;
  font-weight: bold;
  text-align: center;
}

.meal-buttons {
  display: flex;
  justify-content: space-between;
  gap: 0.5rem;
}

.meal-button {
  flex: 1;
  padding: 0.75rem;
  border: none;
  border-radius: 0.375rem;
  background-color: #d9f99d;
  cursor: pointer;
  font-size: 1rem;
  font-weight: bold;
}
</style>
