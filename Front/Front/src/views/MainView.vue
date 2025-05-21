<template>
  <div class="main-layout">
    <LeftSidebar />

    <main class="calendar-area">
      <div class="calendar-header">
        <button class="nav-btn" @click="prevMonth">«</button>
        <span class="calendar-title">{{ currentTitle }}</span>
        <button class="nav-btn" @click="nextMonth">»</button>
      </div>
      <FullCalendar ref="calendarRef" :options="calendarOptions" />
    </main>

    <RightSidebar />

    <DietDetailPanel
      v-if="showDetailPanel"
      :date="selectedDate"
      :nutritionTarget="nutritionTarget"
      @close="showDetailPanel = false"
      @openRegister="openRegisterFromDetail"
    />

    <DietRegisterPanel
      v-if="showRegisterPanel"
      :date="registerInfo.date"
      :mealType="registerInfo.mealType"
      @close="showRegisterPanel = false"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import LeftSidebar from '@/components/LeftSidebar.vue'
import RightSidebar from '@/components/RightSidebar.vue'
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import DietDetailPanel from '@/components/DietDetailPanel.vue'
import DietRegisterPanel from '@/components/DietRegisterPanel.vue'

const selectedDate = ref(null)
const showDetailPanel = ref(false)
const nutritionTarget = ref(null)

const showRegisterPanel = ref(false)
const registerInfo = ref({ date: '', mealType: '' })

async function openDetail(date) {
  selectedDate.value = date
  showDetailPanel.value = true

  const userId = localStorage.getItem('userId')
  const token = localStorage.getItem('accessToken') // 추가

  try {
    const res = await axios.get('http://localhost:8080/api/nutrition/target', {
      params: { userId },
      headers: {
        Authorization: `Bearer ${token}` // 추가
      }
    })
    nutritionTarget.value = res.data
  } catch (e) {
    console.error('권장 섭취량 불러오기 실패:', e)
    nutritionTarget.value = null
  }
}

function openRegisterFromDetail({ date, mealType }) {
  showDetailPanel.value = false
  registerInfo.value = { date, mealType }
  showRegisterPanel.value = true
}

function getLeafIcon(score) {
  if (score >= 80) return 'healthyLeaf.png'
  if (score >= 50) return 'sosoLeaf.png'
  return 'badLeaf.png'
}

const calendarRef = ref(null)
const currentTitle = ref('')
const scoreMap = ref({})

const updateTitle = () => {
  const calendarApi = calendarRef.value.getApi()
  const date = calendarApi.getDate()
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  currentTitle.value = `${year}년 ${month}월`
}

const fetchMonthlyData = async () => {
  const token = localStorage.getItem('accessToken')
  const userId = localStorage.getItem('userId')

  console.log('토큰 확인:', token)

  try {
    const calendarApi = calendarRef.value.getApi()
    const startDate = calendarApi.view.currentStart.toISOString().split('T')[0]
    const endDate = calendarApi.view.currentEnd.toISOString().split('T')[0]

    const res = await axios.get('http://localhost:8080/api/diet/summary/monthly', {
      params: { userId, startDate, endDate },
      headers: {
        Authorization: `Bearer ${token}`
      }
    })

    scoreMap.value = Object.fromEntries(
      res.data.map(item => [item.date, { score: item.score, kcal: item.totalKcal }])
  )
  } catch (e) {
    console.error('식단 요약 데이터 불러오기 실패:', e)
  }
}

const prevMonth = () => {
  calendarRef.value.getApi().prev()
  updateTitle()
  fetchMonthlyData()
}

const nextMonth = () => {
  calendarRef.value.getApi().next()
  updateTitle()
  fetchMonthlyData()
}

onMounted(() => {
  updateTitle()
  fetchMonthlyData()
})

const calendarOptions = ref({
  plugins: [dayGridPlugin],
  initialView: 'dayGridMonth',
  height: 700,
  fixedWeekCount: true,
  dayMaxEventRows: true,
  headerToolbar: false,
  dayCellDidMount(arg) {
    const localDate = new Date(arg.date.getTime() - arg.date.getTimezoneOffset() * 60000)
    const dateStr = localDate.toISOString().split('T')[0]
    const info = scoreMap.value[dateStr]

    const frameEl = arg.el.querySelector('.fc-daygrid-day-frame')
    if (!frameEl) return

    frameEl.style.cursor = 'pointer'
    frameEl.addEventListener('click', () => {
      openDetail(dateStr)
    })

    if (info) {
      const wrapper = document.createElement('div')
      wrapper.classList.add('custom-cell')
      wrapper.innerHTML = `
        <img src='/${getLeafIcon(info.score)}' class='icon-img leaf'/>
        <div class='bottom-row'>
          <img src='/carrot.png' class='icon-img carrotBody'/>
          <div class='kcal-text'>${info.kcal} kcal</div>
        </div>
      `
      frameEl.appendChild(wrapper)
    }
  }
})
</script>

<style>
.main-layout {
  display: flex;
  height: 100vh;
}

.calendar-area {
  flex-grow: 1;
  padding: 30px;
  background-color: #fff;
  min-width: 0;
}

.calendar-header {
  font-family: 'Apple SD Gothic Neo', sans-serif;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  font-size: 40px;
  font-weight: bold;
  margin-bottom: 16px;
}

.nav-btn {
  font-size: 40px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px 12px;
}

.fc-daygrid-day {
  height: 100px !important;
  vertical-align: top !important;
}
.fc-daygrid-day-frame {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
  padding: 4px;
  box-sizing: border-box;
  overflow: hidden;
}

.custom-cell {
  margin-top: 4px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 2px;
  width: 100%;
  flex-shrink: 1;
}
.bottom-row {
  display: flex;
  align-items: center;
  gap: 0px;
}
.icon-img {
  object-fit: contain;
  display: block;
  width: 20px;
  height: 25px;
}
.kcal-text {
  font-size: 11px;
  color: #333;
  margin-left: 4px;
}
.main-layout > * {
  min-width: 180px;
}
</style>
