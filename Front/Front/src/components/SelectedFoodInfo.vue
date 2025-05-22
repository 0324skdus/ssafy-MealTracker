<!-- SelectedFoodInfo.vue -->
<template>
  <div class="selected-food-info">
    <h4>{{ food.name }} (100g 기준)</h4>
    <p>
      {{ food.kcal }} kcal / 탄: {{ food.carbo }} / 단: {{ food.protein }} / 지: {{ food.fat }}
    </p>

    <label>
      섭취량(g):
      <input type="number" v-model.number="weight" min="1" />
    </label>

    <div v-if="weight">
      <p>예상 총 섭취:</p>
      <ul>
        <li>칼로리: {{ calc(food.kcal) }} kcal</li>
        <li>탄수화물: {{ calc(food.carbo) }} g</li>
        <li>단백질: {{ calc(food.protein) }} g</li>
        <li>지방: {{ calc(food.fat) }} g</li>
      </ul>
    </div>

    <button @click="addDiet">추가하기</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({ food: Object })
const emit = defineEmits(['addDiet'])

const weight = ref(100)

const calc = (val) => ((val * weight.value) / 100).toFixed(1)

function addDiet() {
  if (weight.value <= 0) return
  emit('addDiet', {
    ...props.food,
    weight: weight.value,
    kcal: +calc(props.food.kcal),
    carbo: +calc(props.food.carbo),
    protein: +calc(props.food.protein),
    fat: +calc(props.food.fat),
  })
  weight.value = 100 // 초기화
}
</script>

<style scoped>
.selected-food-info {
  border: 1px solid #ccc;
  padding: 16px;
  margin-bottom: 20px;
  border-radius: 8px;
}
input {
  margin-left: 8px;
  width: 80px;
}
button {
  margin-top: 12px;
  padding: 8px 12px;
  background-color: #ff9800;
  color: white;
  border: none;
  border-radius: 4px;
}
</style>
