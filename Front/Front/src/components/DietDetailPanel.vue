<template>
  <div class="backdrop" @click.self="close">
    <div class="panel">
      <!-- 날짜 -->
      <div class="header">
        <h2 class="date-title">{{ date }} 식단 상세</h2>
        <button class="close-btn" @click="close">X</button>
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
        <div class="nutrient kcal">총 칼로리: {{ nutrition.kcal }} / {{ nutritionTarget.kcal }} kcal</div>
        <div class="nutrient carb">탄수화물: {{ nutrition.carb }} / {{ nutritionTarget.carb }}g</div>
        <div class="nutrient protein">단백질: {{ nutrition.protein }} / {{ nutritionTarget.protein }}g</div>
        <div class="nutrient fat">지방: {{ nutrition.fat }} / {{ nutritionTarget.fat }}g</div>
        <div class="nutrient fiber">식이섬유: {{ nutrition.fiber }} / {{ nutritionTarget.fiber }}g</div>
        <div class="nutrient sugar">당류: {{ nutrition.sugar }} / {{ nutritionTarget.sugar }}g</div>
      </div>

      <!-- 식단 상세 (더미 or 추후 API 연동 예정) -->
      <div class="meal-section">
        <p><strong>아침:</strong> {{ meal.breakfast || '등록된 식단 없음' }}</p>
        <p><strong>점심:</strong> {{ meal.lunch || '등록된 식단 없음' }}</p>
        <p><strong>저녁:</strong> {{ meal.dinner || '등록된 식단 없음' }}</p>
        <p><strong>간식:</strong> {{ meal.snack || '등록된 식단 없음' }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  date: String,
  nutritionTarget: Object
})

const emit = defineEmits(['close', 'openRegister'])

const nutrition = {
  kcal: 0,
  carb: 0,
  protein: 0,
  fat: 0,
  fiber: 0,
  sugar: 0
}

const meal = {
  breakfast: '',
  lunch: '',
  dinner: '',
  snack: ''
}

function close() {
  emit('close')
}

function register(mealType) {
  emit('openRegister', { date: props.date, mealType })
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

.meal-section {
  margin-top: 30px;
}
@keyframes slideIn {
  from { transform: translateX(100%); }
  to { transform: translateX(0); }
}
</style>
