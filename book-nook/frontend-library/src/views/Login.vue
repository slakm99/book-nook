<template>
  <div class="login-page">
    <div class="login-hero">
      <p class="eyebrow">BOOKNOOK</p>
      <h1>在校园里，遇见下一本会改变你的书。</h1>
      <p class="hero-text">馆藏、借阅、预约、阅读目标与推荐，都收进一个温暖的阅读空间。</p>
      <div class="floating-books">
        <span>文学</span>
        <span>算法</span>
        <span>心理</span>
        <span>艺术</span>
      </div>
    </div>
    <el-card class="login-card" shadow="never">
      <div class="login-title">
        <div class="brand-mark">书</div>
        <div>
          <h2>书屿 BookNook</h2>
          <p>校园智能借阅与阅读成长系统</p>
        </div>
      </div>
      <el-form :model="form" @keyup.enter="handleLogin">
        <el-form-item>
          <el-input v-model="form.username" size="large" placeholder="用户名：admin / librarian / reader">
            <template #prefix><el-icon><User /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" size="large" type="password" show-password placeholder="密码：123456">
            <template #prefix><el-icon><Lock /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-button class="login-button" size="large" :loading="loading" @click="handleLogin">进入阅读空间</el-button>
      </el-form>
      <div class="login-tips">默认账号：admin / 123456，reader / 123456</div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Lock, User } from '@element-plus/icons-vue'
import { login } from '../api/auth'
import { setLogin } from '../utils/auth'

const router = useRouter()
const loading = ref(false)
const form = reactive({ username: 'admin', password: '123456' })

async function handleLogin() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const res = await login(form)
    setLogin(res.data)
    ElMessage.success('登录成功，欢迎来到书屿')
    router.push(res.data.role === 'READER' ? '/reader-home' : '/dashboard')
  } finally {
    loading.value = false
  }
}
</script>
