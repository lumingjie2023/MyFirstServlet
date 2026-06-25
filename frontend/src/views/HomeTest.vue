<template>
  <div style="padding: 40px;">
    <h1>首页加载成功！</h1>
    <p>欢迎使用大学成绩管理系统</p>
    <p>当前用户：{{ userRole }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const userRole = ref('')

onMounted(() => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  userRole.value = user.role || '未登录'
  console.log('HomeSimple loaded, user:', user)
})
</script>
