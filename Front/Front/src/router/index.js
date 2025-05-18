import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import SignUpView from '../views/SignUpView.vue'
import MainView from '../views/MainView.vue'
import ProfileEditView from '../views/ProfileEditView.vue'

const routes = [
  { path: '/', component: LoginView },
  { path: '/signup', component: SignUpView },
  { path: '/main', component: MainView },
  { path: '/profile-edit', component: ProfileEditView }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
