import { createRouter, createWebHistory } from 'vue-router'
import { getToken, getUser } from '../utils/auth'

const routes = [
  { path: '/login', component: () => import('../views/Login.vue') },
  {
    path: '/',
    component: () => import('../layout/MainLayout.vue'),
    redirect: '/books',
    children: [
      { path: '/dashboard', component: () => import('../views/Dashboard.vue'), meta: { title: '仪表盘' } },
      { path: '/books', component: () => import('../views/BookList.vue'), meta: { title: '图书馆藏' } },
      { path: '/categories', component: () => import('../views/CategoryManage.vue'), meta: { title: '图书分类' } },
      { path: '/readers', component: () => import('../views/ReaderManage.vue'), meta: { title: '读者管理' } },
      { path: '/borrow', component: () => import('../views/BorrowManage.vue'), meta: { title: '借阅办理' } },
      { path: '/reservations', component: () => import('../views/ReservationManage.vue'), meta: { title: '预约管理' } },
      { path: '/inventory-logs', component: () => import('../views/InventoryLog.vue'), meta: { title: '馆藏日志' } },
      { path: '/statistics', component: () => import('../views/Statistics.vue'), meta: { title: '数据统计' } },
      { path: '/reader-home', component: () => import('../views/Dashboard.vue'), meta: { title: '阅读首页' } },
      { path: '/my-borrow', component: () => import('../views/BorrowManage.vue'), meta: { title: '我的借阅' } },
      { path: '/my-reservations', component: () => import('../views/ReservationManage.vue'), meta: { title: '我的预约' } },
      { path: '/reading-goal', component: () => import('../views/ReadingGoal.vue'), meta: { title: '阅读目标' } },
      { path: '/recommendation', component: () => import('../views/Recommendation.vue'), meta: { title: '猜你喜欢' } },
      { path: '/profile', component: () => import('../views/Placeholder.vue'), meta: { title: '个人中心' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path !== '/login' && !getToken()) {
    next('/login')
    return
  }
  if (to.path === '/login' && getToken()) {
    next(getUser().role === 'READER' ? '/reader-home' : '/dashboard')
    return
  }
  next()
})

export default router
