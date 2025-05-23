<template>
  <div class="diet-preview-list" v-if="dietList.length > 0">
    <h4>등록 예정 식단</h4>
    <ul>
      <li v-for="(item, index) in dietList" :key="item.dietId">
        {{ item.foodName }} - ({{ item.kcal }}kcal)
        <button @click="deleteDiet(item.dietId)">삭제</button>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import axios from 'axios'

const props = defineProps({
  dietList: Array
})
const emit = defineEmits(['deleted']) // ⬅ 삭제 후 상위에 알림용 emit

const localList = ref([...props.dietList])

watch(() => props.dietList, (newVal) => {
  localList.value = [...newVal]
})

const deleteDiet = async (dietId) => {
  try {
    const token = localStorage.getItem('accessToken')

    const response = await axios.delete(`/api/diet/${dietId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })

    if (response.status === 200) {
      alert('삭제되었습니다.')
      emit('deleted') // ⬅ 삭제 후 상위에게 다시 조회 요청
    } else {
      alert('삭제 실패')
    }
  } catch (err) {
    alert('삭제 중 오류 발생')
    console.error(err)
  }
}
</script>

<style scoped>
.diet-preview-list {
  margin-bottom: 20px;
}
li {
  margin: 8px 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
button {
  padding: 4px 8px;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
}
</style>
