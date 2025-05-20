<template>
  <div class="main-layout">
    <LeftSidebar />

    <main class="calendar-area">
      <!-- 상단 달 이동 헤더 -->
      <div class="calendar-header">
        <button class="nav-btn" @click="prevMonth">«</button>
        <span class="calendar-title">{{ currentTitle }}</span>
        <button class="nav-btn" @click="nextMonth">»</button>
      </div>
      <FullCalendar ref="calendarRef" :options="calendarOptions" />
    </main>

    <RightSidebar />
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import LeftSidebar from '@/components/LeftSidebar.vue'
import RightSidebar from '@/components/RightSidebar.vue'
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'

// 점수에 따라 아이콘 경로 반환
function getLeafIcon(score) {
  if (score >= 80) return 'healthyLeaf.png'
  if (score >= 50) return 'sosoLeaf.png'
  return 'badLeaf.png'
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
    console.log('응답 데이터:', res.data)  // 이 줄 추가

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

// 다음달 이동
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
  headerToolbar: false, // 기본 헤더 숨김

  // 각 셀 렌더링 이후 DOM 커스터마이징
  // utf 날짜 시차 조정
  dayCellDidMount(arg) {
    const localDate = new Date(arg.date.getTime() - arg.date.getTimezoneOffset() * 60000)
    const dateStr = localDate.toISOString().split('T')[0]
    const info = scoreMap.value[dateStr]

    const frameEl = arg.el.querySelector('.fc-daygrid-day-frame')
    if (!frameEl) return

    // 날짜칸 클릭하면 링크이동(날짜 정보가 담긴 포스트맨에코)
    frameEl.style.cursor = 'pointer'
    frameEl.addEventListener('click', () => {
      window.location.href = `https://postman-echo.com/get?date=${dateStr}`
    })

    // 점수 정보가 있으면 아이콘표시
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
/* 전체 화면 구성 */
.main-layout {
  display: flex;
  height: 100vh;
}

/* 중앙 캘린더 영역 */
.calendar-area {
  flex-grow: 1;
  padding: 30px;
  background-color: #fff;
  min-width: 0;
}

/* 달력헤더 */
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

/* 달 이동 버튼 */
.nav-btn {
  font-size: 40px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px 12px;
}

/* 아이콘 넣으면 칸 크기 변해서 고정한 날짜칸 */
.fc-daygrid-day {
  height: 100px !important;
  vertical-align: top !important;
}

/* 날짜 셀 안쪽 프레임 설정 */
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

/* 커스텀 셀 콘텐츠 */
.custom-cell {
  margin-top: 4px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 2px;
  width: 100%;
  flex-shrink: 1;
}

/* 당근 아이콘+칼로리*/
.bottom-row {
  display: flex;
  align-items: center;
  gap: 0px;
}

/* 공통 아이콘 스타일 */
.icon-img {
  object-fit: contain;
  display: block;
  width: 20px;
  height: 25px;
}

/* 칼로리 텍스트 */
.kcal-text {
  font-size: 11px;
  color: #333;
  margin-left: 4px; /* 아이콘과 텍스트만 약간 띄움 */
}

/* 좌우 사이드바 최소 폭 보장 */
.main-layout > * {
  min-width: 180px;
}
</style>
