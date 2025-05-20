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
/*백에서 한달치 정보 반환하는api생성, 날짜 범위를 받음
프론트에서 캘린더 정보 이용해서 날짜 범위를 정한 api를 보내면
백에서 그 날짜정보만큼의 범위로 데이터 잘라서 전달해줌
반환데이터 형식은 아래 더미데이터 형식 따름름*/ 
// 더미 점수 데이터
const scoreData = [
  { date: '2025-05-26', score: 85, kcal: 1800 },
  { date: '2025-05-27', score: 65, kcal: 1500 },
  { date: '2025-05-18', score: 40, kcal: 1200 }
]
// 기본적인 dayClick방식으로 쓰니 아이콘 있는 셀만 클릭되고 빈셀은 클릭이 안됨 풀캘린더 자체버그
// dayClick는 클릭이벤트만다룸, dayCellDidMount로 클릭+셀 디자인 커스텀 
// 날짜 기반 점수 맵으로 변환
const scoreMap = Object.fromEntries(
  scoreData.map(item => [item.date, item])
)

const calendarRef = ref(null) // FullCalendar 접근용 ref
const currentTitle = ref('')  // 현재 년월 표시용

// 달 표시 기본형 영어길래 한글로 바꿔봄, 영어가 더 나을거같으면 원복하겠음
const updateTitle = () => {
  const calendarApi = calendarRef.value.getApi()
  const date = calendarApi.getDate()
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  currentTitle.value = `${year}년 ${month}월`
}

// 이전달 이동
const prevMonth = () => {
  calendarRef.value.getApi().prev()
  updateTitle()
}

// 다음달 이동
const nextMonth = () => {
  calendarRef.value.getApi().next()
  updateTitle()
}

// 컴포넌트 마운트 시 타이틀 초기화
onMounted(() => {
  updateTitle()
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
    const info = scoreMap[dateStr]

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
  height: 20px;
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
