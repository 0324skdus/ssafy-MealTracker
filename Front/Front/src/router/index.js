import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import SignUpView from '../views/SignUpView.vue' // 추가

const routes = [
  { path: '/', component: LoginView },
  { path: '/signup', component: SignUpView } // 추가
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
