import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import SignUpView from '../views/SignUpView.vue'
import MainView from '../views/MainView.vue'
import BoardView from '@/views/BoardView.vue'
import ProfileEdit from '@/views/ProfileEditView.vue'
import WriteView from '@/views/WriteView.vue'
import CommunityDetail from '@/views/CommunityDetail.vue'
import MyPageView from '@/views/MyPageView.vue'
import FindIdView from '@/views/FindIdView.vue'
import FindPasswordView from '@/views/FindPasswordView.vue'
import UserInfoEdit from '@/views/UserInfoEditView.vue'

const routes = [
  { path: '/', component: LoginView },
  { path: '/signup', component: SignUpView },
  { path: '/main', component: MainView },
  { path: '/board', component: BoardView },
  { path: '/profile-edit', component: ProfileEdit },
  { path: '/write', component: WriteView },
  { path: '/edit/:id', component: WriteView },
  { path: '/community/:id', name: 'CommunityDetail', component: CommunityDetail },
  { path: '/mypage', component: MyPageView },
  { path: '/find-id', component: FindIdView },
  { path: '/find-password', component: FindPasswordView },
  { path: '/userInfo-edit', component: UserInfoEdit }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
