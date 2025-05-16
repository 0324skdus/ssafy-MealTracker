<template>
  <div class="signup-wrapper">
    <div class="signup-container">
      <div class="logo-box">
        <img src="/logo.png" alt="logo" class="logo" />
      </div>

      <!-- 1단계 -->
      <div v-if="step === 1" class="step-box">
        <div class="id-box">
          <input placeholder="ID를 입력하세요" class="input-box id-input" />
          <button class="check-button">중복확인</button>
        </div>
        <input type="password" placeholder="비밀번호를 입력하세요" class="input-box" />
        <input type="password" placeholder="비밀번호를 다시 입력해주세요" class="input-box" />
        <input type="email" placeholder="email을 입력해주세요" class="input-box" />
        <div class="button-center">
          <button @click="step = 2" class="submit-button">다음</button>
        </div>
      </div>

      <!-- 2단계 -->
      <div v-else class="step-box">
        <div class="row-box">
          <div class="column-box">
            <label class="label">성별</label>
            <div class="radio-group">
              <label class="radio-box"><input type="radio" name="gender" /> 남</label>
              <label class="radio-box"><input type="radio" name="gender" /> 여</label>
            </div>
          </div>
          <div class="column-box">
            <label class="label">나이(만)</label>
            <div class="input-wrapper">
              <button @click="age = Math.max(0, age - 1)" class="inner-button">-</button>
              <input v-model="age" class="input-box padded no-spin" type="number" placeholder="나이" />
              <button @click="age++" class="inner-button">+</button>
            </div>
          </div>
        </div>

        <div class="row-box">
          <div class="column-box">
            <label class="label">키 (cm)</label>
            <div class="input-wrapper">
              <button @click="adjustValue('height', -1)" class="inner-button">-</button>
              <input
                v-model="height"
                @click="handleCursor($event, 'height')"
                @input="formatValue('height')"
                class="input-box padded no-spin"
                placeholder="키"
              />
              <button @click="adjustValue('height', +1)" class="inner-button">+</button>
            </div>
          </div>

          <div class="column-box">
            <label class="label">체중 (kg)</label>
            <div class="input-wrapper">
              <button @click="adjustValue('weight', -1)" class="inner-button">-</button>
              <input
                v-model="weight"
                @click="handleCursor($event, 'weight')"
                @input="formatValue('weight')"
                class="input-box padded no-spin"
                placeholder="체중"
              />
              <button @click="adjustValue('weight', +1)" class="inner-button">+</button>
            </div>
          </div>
        </div>

        <div class="activity-box">
          <div class="label">평소 활동량이 얼마나 되나요?</div>
          <label><input type="radio" name="activity" /> 오래 앉아서 일하고 운동을 거의 하지 않습니다</label>
          <label><input type="radio" name="activity" /> 가끔 걷거나 가벼운 활동을 합니다</label>
          <label><input type="radio" name="activity" /> 몸을 많이 쓰는 일을 하거나 운동을 규칙적으로 합니다</label>
        </div>

        <div class="button-row">
          <button @click="step = 1" class="prev-button">이전</button>
          <button class="submit-button">가입하기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const step = ref(1)
const age = ref('')
const height = ref('')
const weight = ref('')
const cursorTarget = ref('int')

function handleCursor(event, type) {
  const pos = event.target.selectionStart
  const dot = (type === 'height' ? height : weight).value.indexOf('.')
  cursorTarget.value = pos <= dot || dot === -1 ? 'int' : 'dec'
}

function formatValue(type) {
  let val = parseFloat(type === 'height' ? height.value : weight.value)
  if (isNaN(val) || val < 0) val = 0
  val = Math.floor(val * 10) / 10
  const fixed = val.toFixed(1)
  if (type === 'height') height.value = fixed
  if (type === 'weight') weight.value = fixed
}

function adjustValue(type, delta) {
  let raw = type === 'height' ? height.value : weight.value
  let val = parseFloat(raw)
  if (isNaN(val)) val = 0
  let [intPart, decPart] = val.toFixed(1).split('.').map(Number)

  if (cursorTarget.value === 'int') {
    intPart = Math.max(0, intPart + delta)
  } else {
    decPart = Math.max(0, Math.min(9, decPart + delta))
  }

  const newVal = `${intPart}.${decPart}`
  if (type === 'height') height.value = newVal
  if (type === 'weight') weight.value = newVal
}
</script>

<style>
.signup-wrapper {
  width: 100vw;
  height: 100vh;
  background-color: white;
  display: flex;
  justify-content: center;
  align-items: center;
}

.signup-container {
  width: 100%;
  max-width: 28rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1.5rem;
  gap: 1rem;
}

.logo-box {
  width: 100%;
  display: flex;
  justify-content: center;
  margin-bottom: 1rem;
}

.logo {
  width: 10rem;
  max-width: 100%;
  height: auto;
  object-fit: contain;
}

.step-box {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
}

.input-box {
  width: 100%;
  height: 2.5rem;
  border-radius: 0.375rem;
  background-color: #d9f99d;
  padding-left: 1rem;
  font-size: 1rem;
  color: #374151;
  border: none;
}

.input-box::placeholder {
  color: #9ca3af;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.padded {
  text-align: center;
  padding-left: 1.5rem;
  padding-right: 1.5rem;
}

.inner-button {
  width: 1.5rem;
  height: 1.5rem;
  font-size: 1rem;
  font-weight: bold;
  background-color: #facc15;
  border: none;
  border-radius: 0.25rem;
  cursor: pointer;
}

.inner-button:first-child {
  position: absolute;
  left: 0.3rem;
}

.inner-button:last-child {
  position: absolute;
  right: 0.3rem;
}

.no-spin::-webkit-inner-spin-button,
.no-spin::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
.no-spin {
  -moz-appearance: textfield;
}

.row-box {
  display: flex;
  width: 100%;
  gap: 1rem;
}

.column-box {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.label {
  font-weight: bold;
  font-size: 0.875rem;
  color: #111827;
}

.radio-group {
  display: flex;
  gap: 1rem;
}

.radio-box {
  background-color: #d9f99d;
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
}

.activity-box {
  background-color: #d9f99d;
  border-radius: 0.375rem;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.button-row {
  display: flex;
  width: 100%;
  justify-content: space-between;
  gap: 0.5rem;
}

.button-center {
  width: 100%;
  display: flex;
  justify-content: center;
}

.prev-button {
  width: 48%;
  height: 2.5rem;
  background-color: #f3f4f6;
  border-radius: 0.375rem;
  border: none;
  font-size: 1rem;
  color: #111827;
  cursor: pointer;
}

.submit-button {
  width: 48%;
  height: 2.5rem;
  background-color: #bef264;
  border-radius: 0.375rem;
  box-shadow: 0 1px 2px rgba(0,0,0,0.1);
  color: #1f2937;
  font-size: 1rem;
  cursor: pointer;
}

.id-box {
  display: flex;
  gap: 0.5rem;
}

.id-input {
  flex: 1;
}

.check-button {
  width: 6rem;
  background-color: #86efac;
  border-radius: 0.375rem;
  border: none;
  font-size: 0.875rem;
  color: #1f2937;
  cursor: pointer;
}
</style>
