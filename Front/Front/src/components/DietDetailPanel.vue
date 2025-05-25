<template>
  <div class="backdrop" @click.self="emit('close')">
    <div class="panel">
      <!-- 날짜 -->
      <div class="header">
        <h2 class="date-title">{{ date }} 식단 상세</h2>
        <button class="close-btn" @click="emit('close')">X</button>
      </div>

      <!-- 식단 점수 -->
      <div class="score-section">
        <p><strong>오늘의 식단 점수:</strong> {{ score }}점</p>
      </div>

      <!-- 식사 등록 버튼 -->
      <div class="meal-buttons">
        <button @click="register('아침')">아침</button>
        <button @click="register('점심')">점심</button>
        <button @click="register('저녁')">저녁</button>
        <button @click="register('간식')">간식</button>
      </div>

      <!-- 영양성분 요약 -->
      <div class="nutrition-summary" v-if="nutritionTarget">
        <div class="nutrient kcal" :style="{ background: `linear-gradient(to right, #e76f51 ${percent.kcal}%, #eee ${percent.kcal}%)` }">
          총 칼로리: {{ total.kcal }} / {{ nutritionTarget.kcal }} kcal
        </div>
        <div class="nutrient carb" :style="{ background: `linear-gradient(to right, #f9c74f ${percent.carbo}%, #eee ${percent.carbo}%)` }">
          탄수화물: {{ total.carbo.toFixed(1) }} / {{ nutritionTarget.carb }}g
        </div>
        <div class="nutrient protein" :style="{ background: `linear-gradient(to right, #f9844a ${percent.protein}%, #eee ${percent.protein}%)` }">
          단백질: {{ total.protein.toFixed(1) }} / {{ nutritionTarget.protein }}g
        </div>
        <div class="nutrient fat" :style="{ background: `linear-gradient(to right, #90be6d ${percent.fat}%, #eee ${percent.fat}%)` }">
          지방: {{ total.fat.toFixed(1) }} / {{ nutritionTarget.fat }}g
        </div>
        <div class="nutrient fiber" :style="{ background: `linear-gradient(to right, #43aa8b ${percent.fiber}%, #eee ${percent.fiber}%)` }">
          식이섬유: {{ total.fiber.toFixed(1) }} / {{ nutritionTarget.fiber }}g
        </div>
        <div class="nutrient sugar" :style="{ background: `linear-gradient(to right, #577590 ${percent.sugar}%, #eee ${percent.sugar}%)` }">
          당류: {{ total.sugar.toFixed(1) }} / {{ nutritionTarget.sugar }}g
        </div>
        <div class="nutrient transfat" :style="{ background: `linear-gradient(to right, #d62828 ${percent.transfat}%, #eee ${percent.transfat}%)` }">
          트랜스지방: {{ total.transfat.toFixed(1) }} / {{ nutritionTarget.transfat }}g
        </div>
      </div>

      <!-- 식단 상세 -->
      <div class="meal-section">
        <p>아침</p>
        <ul>
          <li v-for="(item, idx) in breakfastList" :key="idx">
            {{ item.foodName }} - {{ item.kcal }} kcal
          </li>
        </ul>

        <p>점심</p>
        <ul>
          <li v-for="(item, idx) in lunchList" :key="idx">
            {{ item.foodName }} - {{ item.kcal }} kcal
          </li>
        </ul>

        <p>저녁</p>
        <ul>
          <li v-for="(item, idx) in dinnerList" :key="idx">
            {{ item.foodName }} - {{ item.kcal }} kcal
          </li>
        </ul>

        <p>간식</p>
        <ul>
          <li v-for="(item, idx) in snackList" :key="idx">
            {{ item.foodName }} - {{ item.kcal }} kcal
          </li>
        </ul>
      </div>

      <!-- 공유 버튼 -->
      <div class="bottom-buttons">
        <button class="share-button" @click="shareToCommunity">공유하기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, computed } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const props = defineProps({
  date: String,
  nutritionTarget: Object,
  dietList: Array,
  score: Number
})

const emit = defineEmits(['close', 'openRegister'])
const router = useRouter()

const groupedDietList = ref({})
const total = ref({ kcal: 0, carbo: 0, protein: 0, fat: 0, fiber: 0, sugar: 0, transfat: 0 })

const breakfastList = computed(() => groupedDietList.value['아침'] || [])
const lunchList = computed(() => groupedDietList.value['점심'] || [])
const dinnerList = computed(() => groupedDietList.value['저녁'] || [])
const snackList = computed(() => groupedDietList.value['간식'] || [])

function register(mealType) {
  const mealData = groupedDietList.value[mealType] || []
  emit('openRegister', {
    date: props.date,
    mealType,
    existingDiet: mealData
  })
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
    calculateGroupedAndTotal(res.data)
  } catch (err) {
    console.error('식단 조회 실패:', err)
    calculateGroupedAndTotal([])
  }
}

function calculateGroupedAndTotal(fetchedList) {
  const grouped = { 아침: [], 점심: [], 저녁: [], 간식: [] }
  const sum = { kcal: 0, carbo: 0, protein: 0, fat: 0, fiber: 0, sugar: 0, transfat: 0 }

  const mealMap = { breakfast: '아침', lunch: '점심', dinner: '저녁', snack: '간식' }

  for (const item of fetchedList) {
    const translated = mealMap[item.mealType]
    if (translated) grouped[translated].push(item)

    sum.kcal += Number(item.kcal) || 0
    sum.carbo += Number(item.carbo) || 0
    sum.protein += Number(item.protein) || 0
    sum.fat += Number(item.fat) || 0
    sum.fiber += Number(item.fiber) || 0
    sum.sugar += Number(item.sugar) || 0
    sum.transfat += Number(item.transfat) || 0
  }

  groupedDietList.value = grouped
  total.value = sum
}

const percent = computed(() => {
  if (!props.nutritionTarget) {
    return { kcal: 0, carbo: 0, protein: 0, fat: 0, fiber: 0, sugar: 0, transfat: 0 }
  }
  return {
    kcal: Math.min((total.value.kcal / props.nutritionTarget.kcal) * 100, 100),
    carbo: Math.min((total.value.carbo / props.nutritionTarget.carb) * 100, 100),
    protein: Math.min((total.value.protein / props.nutritionTarget.protein) * 100, 100),
    fat: Math.min((total.value.fat / props.nutritionTarget.fat) * 100, 100),
    fiber: Math.min((total.value.fiber / props.nutritionTarget.fiber) * 100, 100),
    sugar: Math.min((total.value.sugar / props.nutritionTarget.sugar) * 100, 100),
    transfat: Math.min((total.value.transfat / props.nutritionTarget.transfat) * 100, 100)
  }
})

const score = computed(() => {
  if (!props.nutritionTarget) return 0
  const values = Object.values(percent.value)
  const average = values.reduce((a, b) => a + b, 0) / values.length
  return Math.round(average)
})

async function shareToCommunity() {
  const token = localStorage.getItem('accessToken')
  if (!token) {
    alert("로그인이 필요합니다.")
    return
  }

  const title = prompt("공유할 게시글 제목을 입력해주세요:")
  if (!title || title.trim() === "") {
    alert("제목을 입력해주세요.")
    return
  }

  const formatMealList = (name, list) =>
    list.length ? `🍽️ [${name}]\n` + list.map(i => `- ${i.foodName} (${i.kcal} kcal)`).join('\n') + '\n\n' : ''

const content =
  `📅 ${props.date} 식단 공유\n` +
  `🥗 식단 점수: ${score.value}점\n\n` +
  formatMealList("아침", breakfastList.value) +
  formatMealList("점심", lunchList.value) +
  formatMealList("저녁", dinnerList.value) +
  formatMealList("간식", snackList.value) +
  `📊 [영양소 섭취 요약]\n` +
  `- 칼로리: ${total.value.kcal.toFixed(1)} / ${props.nutritionTarget.kcal.toFixed(0)} kcal\n` +
  `- 탄수화물: ${total.value.carbo.toFixed(1)} / ${props.nutritionTarget.carb.toFixed(0)}g\n` +
  `- 단백질: ${total.value.protein.toFixed(1)} / ${props.nutritionTarget.protein.toFixed(0)}g\n` +
  `- 지방: ${total.value.fat.toFixed(1)} / ${props.nutritionTarget.fat.toFixed(0)}g\n` +
  `- 식이섬유: ${total.value.fiber.toFixed(1)} / ${props.nutritionTarget.fiber.toFixed(0)}g\n` +
  `- 당류: ${total.value.sugar.toFixed(1)} / ${props.nutritionTarget.sugar.toFixed(0)}g\n` +
  `- 트랜스지방: ${total.value.transfat.toFixed(1)} / ${props.nutritionTarget.transfat.toFixed(0)}g`


  try {
    const res = await axios.post('/api/community', {
      title: title,
      content: content,
      communityType: "식단 게시판"
    }, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })

    alert("등록이 완료되었습니다.")
if (confirm("해당 게시글로 이동하시겠습니까?")) {
  const token = localStorage.getItem('accessToken')
  const latestRes = await axios.get('/api/community?sort=최신글', {
    headers: {
      Authorization: `Bearer ${token}`
    }
  })
  const newId = latestRes.data[0].id
  router.push(`/community/${newId}`)
}
  } catch (err) {
    console.error("공유 실패:", err)
    alert("게시글 공유에 실패했습니다.")
  }
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
.date-title {
  font-size: 20px;
  font-weight: bold;
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
.meal-buttons {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 12px;
  margin: 20px 0;
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

.nutrition-summary {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.nutrient {
  padding: 10px;
  border-radius: 8px;
  font-weight: bold;
  color: white;
}
.nutrient.kcal    { background-color: #e76f51; }
.nutrient.carb    { background-color: #f9c74f; }
.nutrient.protein { background-color: #f9844a; }
.nutrient.fat     { background-color: #90be6d; }
.nutrient.fiber   { background-color: #43aa8b; }
.nutrient.sugar   { background-color: #577590; }
.nutrient.transfat { background-color: #d62828; }

.score-section {
  margin-top: 20px;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.meal-section {
  margin-top: 30px;
}

.bottom-buttons {
  margin-top: auto;
  display: flex;
  justify-content: flex-end;
}

.share-button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #4c8bf5;
  color: white;
  font-weight: bold;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.share-button:hover {
  background-color: #2e6ff2;
}

@keyframes slideIn {
  from { transform: translateX(100%); }
  to { transform: translateX(0); }
}
</style>
