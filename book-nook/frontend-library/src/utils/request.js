import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, logout } from './auth'
import router from '../router'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.request.use(config => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

request.interceptors.response.use(
  response => {
    const body = response.data
    if (body && body.code && body.code !== 200) {
      ElMessage.error(body.message || '请求失败')
      if (body.code === 401) {
        logout()
        router.push('/login')
      }
      return Promise.reject(body)
    }
    return body
  },
  error => {
    ElMessage.error(error?.response?.data?.message || '网络连接失败，请确认后端已启动')
    return Promise.reject(error)
  }
)

export default request
