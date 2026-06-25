<template>
  <div class="students">
    <div class="toolbar">
      <input v-model="searchKeyword" placeholder="搜索学生" class="search-input" @keyup.enter="handleSearch" />
      <button v-if="isAdmin" class="add-btn" @click="openAddModal">添加学生</button>
    </div>
    
    <table class="data-table">
      <thead>
        <tr>
          <th>学号</th>
          <th>姓名</th>
          <th>性别</th>
          <th>专业</th>
          <th>班级</th>
          <th>邮箱</th>
          <th v-if="isAdmin">操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="student in students" :key="student.studentId">
          <td>{{ student.studentId }}</td>
          <td>{{ student.name }}</td>
          <td>{{ student.gender }}</td>
          <td>{{ student.major }}</td>
          <td>{{ student.className }}</td>
          <td>{{ student.email }}</td>
          <td v-if="isAdmin">
            <button class="edit-btn" @click="openEditModal(student)">编辑</button>
            <button class="delete-btn" @click="handleDelete(student.studentId)">删除</button>
          </td>
        </tr>
      </tbody>
    </table>
    
    <div v-if="modalVisible" class="modal-overlay" @click.self="modalVisible = false">
      <div class="modal-content">
        <h3>{{ isEdit ? '编辑学生' : '添加学生' }}</h3>
        <div class="form-group">
          <label>学号</label>
          <input v-model="form.studentId" :disabled="isEdit" />
        </div>
        <div class="form-group">
          <label>姓名</label>
          <input v-model="form.name" />
        </div>
        <div class="form-group">
          <label>性别</label>
          <select v-model="form.gender">
            <option value="男">男</option>
            <option value="女">女</option>
          </select>
        </div>
        <div class="form-group">
          <label>专业</label>
          <input v-model="form.major" />
        </div>
        <div class="form-group">
          <label>班级</label>
          <input v-model="form.className" />
        </div>
        <div class="form-group">
          <label>邮箱</label>
          <input v-model="form.email" />
        </div>
        <div class="form-group">
          <label>电话</label>
          <input v-model="form.phone" />
        </div>
        <div class="modal-footer">
          <button class="cancel-btn" @click="modalVisible = false">取消</button>
          <button v-if="isAdmin" class="save-btn" @click="handleSave">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { getAllStudents, addStudent, updateStudent, deleteStudent } from '../api/student'

const students = ref([])
const searchKeyword = ref('')
const modalVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  studentId: '',
  name: '',
  gender: '男',
  major: '',
  className: '',
  email: '',
  phone: ''
})

// 判断是否为管理员
const isAdmin = computed(() => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  return user.role === 'ADMIN'
})

onMounted(async () => {
  await loadStudents()
})

const loadStudents = async () => {
  const response = await getAllStudents()
  students.value = response.data
}

const handleSearch = async () => {
  if (!searchKeyword.value) {
    await loadStudents()
    return
  }
  const response = await getAllStudents()
  students.value = response.data.filter(s => 
    s.studentId.includes(searchKeyword.value) ||
    s.name.includes(searchKeyword.value) ||
    s.major.includes(searchKeyword.value) ||
    s.className.includes(searchKeyword.value)
  )
}

const openAddModal = () => {
  isEdit.value = false
  form.value = {
    studentId: '',
    name: '',
    gender: '男',
    major: '',
    className: '',
    email: '',
    phone: ''
  }
  modalVisible.value = true
}

const openEditModal = (row) => {
  isEdit.value = true
  form.value = { ...row }
  modalVisible.value = true
}

const handleSave = async () => {
  try {
    if (isEdit.value) {
      await updateStudent(form.value)
    } else {
      await addStudent(form.value)
    }
    modalVisible.value = false
    await loadStudents()
    alert('保存成功')
  } catch (error) {
    alert('保存失败')
  }
}

const handleDelete = async (id) => {
  if (confirm('确定要删除该学生吗？')) {
    try {
      await deleteStudent(id)
      await loadStudents()
      alert('删除成功')
    } catch (error) {
      alert('删除失败')
    }
  }
}
</script>

<style scoped>
.students {
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
