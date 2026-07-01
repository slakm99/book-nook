import { createRouter, createWebHistory } from 'vue-router'
import { getToken, getUser } from '../utils/auth'

const routes = [
  { path: '/login', component: () => import('../views/Login.vue') },
  {
    path: '/',
    component: () => import('../layout/MainLayout.vue'),
    redirect: '/books',
    children: [
      { path: '/dashboard', component: () => import('../views/Dashboard.vue'), meta: { title: '仪表盘', roles: ['ADMIN', 'LIBRARIAN'] } },
      { path: '/books', component: () => import('../views/BookList.vue'), meta: { title: '图书馆藏', roles: ['ADMIN', 'LIBRARIAN', 'READER'] } },
      { path: '/categories', component: () => import('../views/CategoryManage.vue'), meta: { title: '图书分类', roles: ['ADMIN', 'LIBRARIAN'] } },
      { path: '/readers', component: () => import('../views/ReaderManage.vue'), meta: { title: '读者管理', roles: ['ADMIN', 'LIBRARIAN'] } },
      { path: '/borrow', component: () => import('../views/BorrowManage.vue'), meta: { title: '借阅办理', roles: ['ADMIN', 'LIBRARIAN'] } },
      { path: '/reservations', component: () => import('../views/ReservationManage.vue'), meta: { title: '预约管理', roles: ['ADMIN', 'LIBRARIAN'] } },
      { path: '/inventory-logs', component: () => import('../views/InventoryLog.vue'), meta: { title: '馆藏日志', roles: ['ADMIN', 'LIBRARIAN'] } },
      { path: '/statistics', component: () => import('../views/Statistics.vue'), meta: { title: '数据统计', roles: ['ADMIN'] } },
      { path: '/reader-home', component: () => import('../views/ReaderHome.vue'), meta: { title: '阅读首页', roles: ['READER'] } },
      { path: '/my-borrow', component: () => import('../views/BorrowManage.vue'), meta: { title: '我的借阅', roles: ['READER'] } },
      { path: '/my-reservations', component: () => import('../views/ReservationManage.vue'), meta: { title: '我的预约', roles: ['READER'] } },
      { path: '/reading-goal', component: () => import('../views/ReadingGoal.vue'), meta: { title: '阅读目标', roles: ['READER'] } },
      { path: '/recommendation', component: () => import('../views/Recommendation.vue'), meta: { title: '猜你喜欢', roles: ['READER'] } },
      { path: '/profile', component: () => import('../views/Placeholder.vue'), meta: { title: '个人中心', roles: ['READER'] } }
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
  const user = getUser()
  if (to.meta.roles && !to.meta.roles.includes(user.role)) {
    next(user.role === 'READER' ? '/reader-home' : '/dashboard')
    return
  }
  next()
})

export default router
