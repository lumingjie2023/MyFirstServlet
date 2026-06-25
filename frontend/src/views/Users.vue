<template>
  <div class="users">
    <div class="toolbar">
      <input v-model="searchKeyword" placeholder="搜索用户" class="search-input" @keyup.enter="handleSearch" />
      <button class="add-btn" @click="openAddModal">添加用户</button>
    </div>
    
    <table class="data-table">
      <thead>
        <tr>
          <th>用户名</th>
          <th>真实姓名</th>
          <th>角色</th>
          <th>邮箱</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.username">
          <td>{{ user.username }}</td>
          <td>{{ user.realName }}</td>
          <td>
            <span :class="['role-tag', getRoleClass(user.role)]">{{ getRoleText(user.role) }}</span>
          </td>
          <td>{{ user.email }}</td>
          <td>
            <button class="edit-btn" @click="openEditModal(user)">编辑</button>
            <button class="delete-btn" @click="handleDelete(user.username)">删除</button>
          </td>
        </tr>
      </tbody>
    </table>
    
    <div v-if="modalVisible" class="modal-overlay" @click.self="modalVisible = false">
      <div class="modal-content">
        <h3>{{ isEdit ? '编辑用户' : '添加用户' }}</h3>
        <div class="form-group">
          <label>用户名</label>
          <input v-model="form.username" :disabled="isEdit" />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input type="password" v-model="form.password" :placeholder="isEdit ? '不修改则留空' : '请输入密码'" />
        </div>
        <div class="form-group">
          <label>真实姓名</label>
          <input v-model="form.realName" />
        </div>
        <div class="form-group">
          <label>角色</label>
          <select v-model="form.role">
            <option value="ADMIN">管理员</option>
            <option value="TEACHER">教师</option>
            <option value="STUDENT">学生</option>
          </select>
        </div>
        <div class="form-group">
          <label>邮箱</label>
          <input v-model="form.email" />
        </div>
        <div class="modal-footer">
          <button class="cancel-btn" @click="modalVisible = false">取消</button>
          <button class="save-btn" @click="handleSave">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAllUsers, addUser, updateUser, deleteUser } from '../api/user'

const users = ref([])
const searchKeyword = ref('')
const modalVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  username: '',
  password: '',
  realName: '',
  role: 'STUDENT',
  email: ''
})

onMounted(async () => {
  await loadUsers()
})

const loadUsers = async () => {
  const response = await getAllUsers()
  users.value = response.data
}

const handleSearch = async () => {
  if (!searchKeyword.value) {
    await loadUsers()
    return
  }
  const response = await getAllUsers()
  users.value = response.data.filter(u => 
    u.username.includes(searchKeyword.value) ||
    u.realName.includes(searchKeyword.value)
  )
}

const openAddModal = () => {
  isEdit.value = false
  form.value = {
    username: '',
    password: '',
    realName: '',
    role: 'STUDENT',
    email: ''
  }
  modalVisible.value = true
}

const openEditModal = (row) => {
  isEdit.value = true
  form.value = {
    username: row.username,
    password: '',
    realName: row.realName,
    role: row.role,
    email: row.email
  }
  modalVisible.value = true
}

const handleSave = async () => {
  try {
    if (!form.value.password && !isEdit.value) {
      alert('请输入密码')
      return
    }
    
    const userData = {
      username: form.value.username,
      password: form.value.password || undefined,
      realName: form.value.realName,
      role: form.value.role,
      email: form.value.email
    }
    
    if (isEdit.value) {
      await updateUser(userData)
    } else {
      await addUser(userData)
    }
    modalVisible.value = false
    await loadUsers()
    alert('保存成功')
  } catch (error) {
    alert('保存失败，用户名可能已存在')
  }
}

const handleDelete = async (username) => {
  if (confirm('确定要删除该用户吗？')) {
    try {
      await deleteUser(username)
      await loadUsers()
      alert('删除成功')
    } catch (error) {
      alert('删除失败')
    }
  }
}

const getRoleText = (role) => {
  return role === 'ADMIN' ? '管理员' : role === 'TEACHER' ? '教师' : '学生'
}

const getRoleClass = (role) => {
  return role === 'ADMIN' ? 'role-danger' : role === 'TEACHER' ? 'role-warning' : 'role-success'
}
</script>

<style scoped>
.users {
  padding: 20px;
}

.toolbar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.search-input {
  width: 300px;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.add-btn {
  padding: 8px 16px;
  background: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.add-btn:hover {
  background: #66b1ff;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.data-table th,
.data-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.data-table th {
  background: #f5f7fa;
  font-weight: bold;
}

.data-table tr:hover {
  background: #fafafa;
}

.role-tag {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.role-danger {
  background: #fef0f0;
  color: #f56c6c;
}

.role-warning {
  background: #fdf6ec;
  color: #e6a23c;
}

.role-success {
  background: #f0f9eb;
  color: #67c23a;
}

.edit-btn {
  padding: 4px 8px;
  background: #67c23a;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 5px;
}

.edit-btn:hover {
  background: #85ce61;
}

.delete-btn {
  padding: 4px 8px;
  background: #f56c6c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.delete-btn:hover {
  background: #f78989;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
}

.modal-content h3 {
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
}

.form-group input:disabled {
  background: #f5f5f5;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.cancel-btn {
  padding: 8px 16px;
  background: #eee;
  color: #333;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.save-btn {
  padding: 8px 16px;
  background: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>
