<template>
  <div class="dashboard">
    <aside class="sidebar">
      <div class="logo">
        <h2>成绩管理系统</h2>
      </div>
      <div class="menu">
        <div
          v-for="item in menuItems"
          :key="item.path"
          :class="['menu-item', activeMenu === item.path ? 'active' : '']"
          @click="handleMenuClick(item.path)"
        >
          {{ item.label }}
        </div>
      </div>
    </aside>
    <main class="main-content">
      <header class="header">
        <div class="user-info">
          <span>{{ userRealName }} ({{ roleText }})</span>
          <button class="logout-btn" @click="handleLogout">退出登录</button>
        </div>
      </header>
      <div class="content">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const user = JSON.parse(localStorage.getItem('user') || '{}')
const userRole = ref(user.role || '')
const userRealName = ref(user.realName || '')
const activeMenu = ref('home')

const roleText = computed(() => {
  return userRole.value === 'ADMIN' ? '管理员' : userRole.value === 'TEACHER' ? '教师' : '学生'
})

const menuItems = computed(() => {
  const items = [
    { path: 'home', label: '首页', roles: ['ADMIN', 'TEACHER', 'STUDENT'] },
    { path: 'students', label: '学生管理', roles: ['ADMIN', 'TEACHER'] },
    { path: 'courses', label: '课程管理', roles: ['ADMIN'] },
    { path: 'grades', label: '成绩管理', roles: ['ADMIN', 'TEACHER'] },
    { path: 'mygrades', label: '我的成绩', roles: ['STUDENT'] },
    { path: 'users', label: '用户管理', roles: ['ADMIN'] },
    { path: 'teacher-course', label: '教师课程分配', roles: ['ADMIN'] }
  ]
  return items.filter(item => item.roles.includes(userRole.value))
})

const handleMenuClick = (menu) => {
  activeMenu.value = menu
  router.push(`/dashboard/${menu}`)
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/')
}
</script>

<style scoped>
.dashboard {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 200px;
  background: linear-gradient(180deg, #1a1a2e 0%, #16213e 100%);
  color: white;
  padding: 20px 0;
}

.logo {
  text-align: center;
  padding: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo h2 {
  font-size: 16px;
  margin: 0;
}

.menu {
  padding: 10px 0;
}

.menu-item {
  padding: 12px 20px;
  cursor: pointer;
  transition: all 0.3s;
  color: rgba(255, 255, 255, 0.8);
}

.menu-item:hover {
  background: rgba(255, 255, 255, 0.1);
  color: white;
}

.menu-item.active {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border-left: 3px solid #409eff;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

.header {
  background: white;
  padding: 15px 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: flex-end;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info span {
  font-size: 14px;
  color: #333;
}

.logout-btn {
  padding: 6px 12px;
  background: #f56c6c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.logout-btn:hover {
  background: #e64c4c;
}

.content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}
</style>
