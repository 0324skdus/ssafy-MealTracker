<template>
  <div class="backdrop" @click.self="emit('close')">
    <div class="panel">
      <div class="header">
        <h2>{{ date }} 식단 상세</h2>
        <button class="close-btn" @click="emit('close')">X</button>
      </div>

      <div class="meal-section">
        <div v-for="meal in ['아침', '점심', '저녁', '간식']" :key="meal">
          <p><strong>{{ meal }}:</strong> 
            <span v-if="groupedDietList[meal]">
              {{ groupedDietList[meal].map(item => item.foodName).join(', ') }}
            </span>
            <span v-else>없음</span>
          </p>
        </div>

        <p><strong>총 칼로리:</strong> {{ total.kcal }} kcal</p>
      </div>

      <div class="footer">
        <div class="meal-buttons">
          <button @click="register('아침')">아침</button>
          <button @click="register('점심')">점심</button>
          <button @click="register('저녁')">저녁</button>
          <button @click="register('간식')">간식</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import axios from 'axios'

const props = defineProps({ date: String })
const emit = defineEmits(['close', 'openRegister'])

const dietList = ref([])
const groupedDietList = ref({})
const total = ref({ kcal: 0, carbo: 0, protein: 0, fat: 0, fiber: 0, sugar: 0 })

function register(mealType) {
  emit('openRegister', { date: props.date, mealType })
}
async function fetchDietList() {
  const userId = localStorage.getItem('userId')
  const token = localStorage.getItem('accessToken')
  if (!userId || !props.date || !token) return

  try {
    const res = await axios.get(`/api/diet/user/${userId}/date`, {
      params: { date: props.date },
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    dietList.value = res.data
    calculateGroupedAndTotal()
  } catch (err) {
    console.error('식단 조회 실패:', err)
  }
}

function calculateGroupedAndTotal() {
  const grouped = { 아침: [], 점심: [], 저녁: [], 간식: [] }
  const sum = { kcal: 0, carbo: 0, protein: 0, fat: 0, fiber: 0, sugar: 0 }

  // 영어 mealType → 한글 매핑
  const mealMap = {
    breakfast: '아침',
    lunch: '점심',
    dinner: '저녁',
    snack: '간식'
  }

  for (const item of dietList.value) {
    const translated = mealMap[item.mealType] // 예: 'breakfast' → '아침'
    if (translated) grouped[translated].push(item)

    sum.kcal += Number(item.kcal) || 0
    sum.carbo += Number(item.carbo) || 0
    sum.protein += Number(item.protein) || 0
    sum.fat += Number(item.fat) || 0
    sum.fiber += Number(item.fiber) || 0
    sum.sugar += Number(item.sugar) || 0
  }

  groupedDietList.value = grouped
  total.value = sum
}



watch(() => props.date, fetchDietList, { immediate: true })
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
  width: 420px;
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
.meal-section {
  flex-grow: 1;
  margin-top: 20px;
}
.footer {
  text-align: center;
  margin-top: 20px;
}
.meal-buttons {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 10px;
}
.meal-buttons button {
  padding: 10px 20px;
  border-radius: 100px;
  border: none;
  color: white;
  font-weight: bold;
  cursor: pointer;
  width: 80px;
}
.meal-buttons button:nth-child(1) { background-color: #7fb77e; }
.meal-buttons button:nth-child(2) { background-color: #f8b400; }
.meal-buttons button:nth-child(3) { background-color: #6a67ce; }
.meal-buttons button:nth-child(4) { background-color: #ff8360; }
@keyframes slideIn {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}
</style>
