import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import SignUpView from '../views/SignUpView.vue'
import MainView from '../views/MainView.vue'
import BoardView from '@/views/BoardView.vue'
import ProfileEdit from '@/views/ProfileEditView.vue'
import WriteView from '@/views/WriteView.vue'
import CommunityDetail from '@/views/CommunityDetail.vue'
import MyPageView from '@/views/MyPageView.vue'

const routes = [
  { path: '/', component: LoginView },
  { path: '/signup', component: SignUpView },
  { path: '/main', component: MainView },//메인페이지(캘린더) 링크
  { path: '/board',component: BoardView },
  { path: '/profile-edit', component: ProfileEdit },
   { path: '/write', component: WriteView },
    {
    path: '/community/:id',
    name: 'CommunityDetail',
    component: CommunityDetail
  },
  {
  path: '/edit/:id',
  component: WriteView
},
{ path: '/community/:id', component: CommunityDetail },
   { path: '/mypage', component: MyPageView },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
