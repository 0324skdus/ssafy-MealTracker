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
const detailShouldReopen = ref(false)
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
  detailShouldReopen.value = true // 닫고 다시 열게끔 기억해둠
  registerInfo.value = { date, mealType }
  showRegisterPanel.value = true
}

function getLeafIcon(score) {
  if (score >= 80) return 'healthyLeaf.png'
  if (score >= 50) return 'sosoLeaf.png'
  return 'badLeaf.png'
}

//등록 패널이 닫힐 때 상세 패널 다시 열기 처리
function handlePanelBackdropClick() {
  if (showRegisterPanel.value) {
    showRegisterPanel.value = false
    nextTick(() => {
      if (detailShouldReopen.value) {
        showDetailPanel.value = true
        detailShouldReopen.value = false
      }
    })
  } else {
    showDetailPanel.value = false
  }
}
const calendarRef = ref(null) // FullCalendar 접근용 ref
const currentTitle = ref('')  // 현재 년월 표시용
const scoreMap = ref({}) // 날짜 기반 점수 맵 (API로 가져온 후 갱신됨)

// 달 표시 커스텀
const updateTitle = () => {
  const calendarApi = calendarRef.value.getApi()
  const date = calendarApi.getDate()
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  currentTitle.value = `${year}년 ${month}월`
}

// API에서 한 달치 식단 점수 요약 받아오기
const fetchMonthlyData = async () => {
  try {
    const token = localStorage.getItem('accessToken')
    const userId = localStorage.getItem('userId')

    // 현재 보여지는 캘린더의 전체 날짜 범위 (지난달~다음달 일부 포함)
    const calendarApi = calendarRef.value.getApi()
    const startDate = calendarApi.view.currentStart.toISOString().split('T')[0]
    const endDate = calendarApi.view.currentEnd.toISOString().split('T')[0]

    const res = await axios.get('http://localhost:8080/api/diet/summary/monthly', {
      params: { userId, startDate, endDate },
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    console.log('응답 데이터:', res.data)  // 제출전 삭제필요요

    // 날짜 기준으로 데이터 변환
    scoreMap.value = Object.fromEntries(
      res.data.map(item => [item.date, { score: item.score, kcal: item.totalKcal }])
    )
const currentDate = calendarApi.getDate()
calendarApi.next()
calendarApi.prev()
calendarApi.gotoDate(currentDate)

    calendarApi.render() // 셀 다시 그리기
  } catch (e) {
    console.error('식단 요약 데이터 불러오기 실패:', e)
  }
}

// 이전달 이동
const prevMonth = () => {
  calendarRef.value.getApi().prev()
  updateTitle()
  fetchMonthlyData() // 달 변경시 데이터 다시 로딩
}

const nextMonth = () => {
  calendarRef.value.getApi().next()
  updateTitle()
  fetchMonthlyData() // 달 변경시 데이터 다시 로딩
}

// 컴포넌트 마운트 시 타이틀 및 데이터 초기화
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
.panel-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000; /* 캘린더보다 위 */
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: flex-end; /* 패널 오른쪽에서 슬라이딩 되도록 */
}

</style>
