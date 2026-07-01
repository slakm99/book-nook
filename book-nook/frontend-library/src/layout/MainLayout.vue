<template>
  <el-container class="app-shell">
    <el-aside width="248px" class="sidebar">
      <div class="brand">
        <div class="brand-mark">书</div>
        <div>
          <h1>书屿 BookNook</h1>
          <p>校园智能借阅系统</p>
        </div>
      </div>
      <el-menu :default-active="$route.path" router class="side-menu">
        <template v-for="item in menus" :key="item.path">
          <el-menu-item :index="item.path">
            <el-icon><component :is="item.icon" /></el-icon>
            <span>{{ item.label }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="topbar">
        <div>
          <p class="eyebrow">BOOKNOOK CAMPUS LIBRARY</p>
          <h2>{{ $route.meta.title || '阅读空间' }}</h2>
        </div>
        <div class="user-panel">
          <el-tag effect="plain" class="role-tag">{{ roleName }}</el-tag>
          <span>{{ user.username }}</span>
          <el-button text @click="handleLogout">退出</el-button>
        </div>
      </el-header>
      <el-main class="main-area">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { Collection, DataAnalysis, Files, Reading, User, Tickets, Notebook, TrendCharts, Aim, Star, House } from '@element-plus/icons-vue'
import { getUser, logout } from '../utils/auth'

const router = useRouter()
const user = computed(() => getUser())

const adminMenus = [
  { path: '/dashboard', label: '仪表盘', icon: DataAnalysis },
  { path: '/books', label: '图书馆藏', icon: Collection },
  { path: '/categories', label: '图书分类', icon: Files },
  { path: '/readers', label: '读者管理', icon: User },
  { path: '/borrow', label: '借阅办理', icon: Reading },
  { path: '/reservations', label: '预约管理', icon: Tickets },
  { path: '/inventory-logs', label: '馆藏日志', icon: Notebook },
  { path: '/statistics', label: '数据统计', icon: TrendCharts }
]

const readerMenus = [
  { path: '/reader-home', label: '阅读首页', icon: House },
  { path: '/books', label: '图书探索', icon: Collection },
  { path: '/my-borrow', label: '我的借阅', icon: Reading },
  { path: '/my-reservations', label: '我的预约', icon: Tickets },
  { path: '/reading-goal', label: '阅读目标', icon: Aim },
  { path: '/recommendation', label: '猜你喜欢', icon: Star },
  { path: '/profile', label: '个人中心', icon: User }
]

const menus = computed(() => {
  if (user.value.role === 'READER') return readerMenus
  if (user.value.role === 'LIBRARIAN') return adminMenus.filter(item => item.path !== '/statistics')
  return adminMenus
})
const roleName = computed(() => ({ ADMIN: '管理员', LIBRARIAN: '馆员', READER: '读者' }[user.value.role] || '访客'))

function handleLogout() {
  logout()
  router.push('/login')
}
</script>
