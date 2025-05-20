<template>
  <aside class="right-sidebar">
    <div class="info-box">
      <h3>인기글</h3>
      <ul>
        <li>자취하면서 잘 해먹기</li>
        <li>유지어터 식단 추천</li>
        <li>근성장 식단 템플릿</li>
        <li>닭가슴살없이 근성장</li>
        <li>시판식품으로 저염식</li>
      </ul>
    </div>
    <div class="info-box">
      <h3>최신글</h3>
      <ul>
        <li>오늘의 점심</li>
        <li>직장인 식단</li>
        <li>식단 올리기 임박자</li>
        <li>이상한 글 필터링</li>
        <li>밀가루 줄기 3일차</li>
      </ul>
    </div>
  </aside>
</template>

<style scoped>
.right-sidebar {
  width: 250px;
  background-color: #c7eeb0;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.info-box h3 {
  margin-bottom: 10px;
  font-weight: bold;
}

.info-box ul {
  list-style: none;
  padding: 0;
  font-size: 14px;
}

.info-box li {
  margin-bottom: 6px;
}
</style>
