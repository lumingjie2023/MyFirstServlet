<template>
  <div style="min-height: 100vh; display: flex; justify-content: center; align-items: center; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
    <div style="background: white; padding: 40px; border-radius: 10px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.1); width: 400px;">
      <h2 style="text-align: center; margin-bottom: 30px;">大学成绩管理系统</h2>
      <div style="margin-bottom: 20px;">
        <label style="display: block; margin-bottom: 8px; font-weight: bold;">用户名</label>
        <input
          v-model="username"
          type="text"
          placeholder="请输入用户名"
          style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box;"
          @keyup.enter="handleLogin"
        />
      </div>
      <div style="margin-bottom: 20px;">
        <label style="display: block; margin-bottom: 8px; font-weight: bold;">密码</label>
        <input
          v-model="password"
          type="password"
          placeholder="请输入密码"
          style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box;"
          @keyup.enter="handleLogin"
        />
      </div>
      <button
        @click="handleLogin"
        :disabled="loading"
        style="width: 100%; padding: 12px; background: #409eff; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px;"
      >
        {{ loading ? '登录中...' : '登录' }}
      </button>
      <div v-if="message" style="margin-top: 15px; padding: 10px; border-radius: 4px; background: #f0f0f0; text-align: center;">
        {{ message }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from '../utils/axios'

const router = useRouter()
const username = ref('')
const password = ref('')
const loading = ref(false)
const message = ref('')

const handleLogin = async () => {
  if (!username.value || !password.value) {
    message.value = '请输入用户名和密码'
    return
  }

  loading.value = true
  message.value = ''

  try {
    // 登录前先清空旧的 token，避免使用过期的 token
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    
    console.log('开始登录...')
    const response = await axios.post('/auth/login', {
      username: username.value,
      password: password.value
    })
    console.log('登录成功:', response.data)
    localStorage.setItem('token', response.data.token)
    localStorage.setItem('user', JSON.stringify(response.data))
    message.value = '登录成功！正在跳转...'
    await router.push('/dashboard/home')
  } catch (error) {
    console.error('登录失败:', error)
    if (error.response) {
      console.error('错误状态:', error.response.status)
      console.error('错误数据:', error.response.data)
      message.value = '登录失败: ' + (error.response.data?.message || error.response.data || '请检查用户名和密码')
    } else if (error.request) {
      console.error('没有收到响应:', error.request)
      message.value = '网络错误：无法连接到后端服务器'
    } else {
      message.value = '登录失败: ' + error.message
    }
  } finally {
    loading.value = false
  }
}
</script>
