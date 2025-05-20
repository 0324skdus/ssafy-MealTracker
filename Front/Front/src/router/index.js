import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import SignUpView from '../views/SignUpView.vue'
import MainView from '../views/MainView.vue'
import BoardView from '@/views/BoardView.vue'
import ProfileEdit from '@/views/ProfileEditView.vue'
import WriteView from '@/views/WriteView.vue'

const routes = [
  { path: '/', component: LoginView },
  { path: '/signup', component: SignUpView },
  { path: '/main', component: MainView },//메인페이지(캘린더) 링크
  { path: '/board',component: BoardView },
  { path: '/profile-edit', component: ProfileEdit },
   { path: '/write', component: WriteView }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
