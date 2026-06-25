<template>
  <div class="grades">
    <div class="toolbar">
      <input v-model="searchKeyword" placeholder="搜索成绩" class="search-input" @keyup.enter="handleSearch" />
      <button class="add-btn" @click="openAddModal">录入成绩</button>
    </div>
    
    <table class="data-table">
      <thead>
        <tr>
          <th>学生姓名</th>
          <th>课程名称</th>
          <th>成绩</th>
          <th>等级</th>
          <th>学分</th>
          <th>学期</th>
          <th>考试类型</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="grade in grades" :key="grade.id">
          <td>{{ grade.studentName }}</td>
          <td>{{ grade.courseName }}</td>
          <td>{{ grade.score }}</td>
          <td>{{ grade.gradeLevel }}</td>
          <td>{{ grade.credit }}</td>
          <td>{{ grade.semester }}</td>
          <td>{{ grade.examType }}</td>
          <td>
            <button class="edit-btn" @click="openEditModal(grade)">编辑</button>
            <button class="delete-btn" @click="handleDelete(grade.id)">删除</button>
          </td>
        </tr>
      </tbody>
    </table>
    
    <div v-if="modalVisible" class="modal-overlay" @click.self="modalVisible = false">
      <div class="modal-content">
        <h3>{{ isEdit ? '编辑成绩' : '录入成绩' }}</h3>
        <div class="form-group">
          <label>学生</label>
          <select v-model="form.studentId">
            <option value="">请选择学生</option>
            <option v-for="s in students" :key="s.studentId" :value="s.studentId">{{ s.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>课程</label>
          <select v-model="form.courseId">
            <option value="">请选择课程</option>
            <option v-for="c in courses" :key="c.courseId" :value="c.courseId">{{ c.courseName }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>成绩</label>
          <input type="number" v-model="form.score" />
        </div>
        <div class="form-group">
          <label>学期</label>
          <input v-model="form.semester" />
        </div>
        <div class="form-group">
          <label>考试类型</label>
          <input v-model="form.examType" />
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
import { getAllGrades, addGrade, updateGrade, deleteGrade } from '../api/grade'
import { getAllStudents } from '../api/student'
import { getAllCourses } from '../api/course'

const grades = ref([])
const students = ref([])
const courses = ref([])
const searchKeyword = ref('')
const modalVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  id: null,
  studentId: '',
  courseId: '',
  score: 0,
  semester: '',
  examType: '期末考试'
})

onMounted(async () => {
  await Promise.all([loadGrades(), loadStudents(), loadCourses()])
})

const loadGrades = async () => {
  const response = await getAllGrades()
  grades.value = response.data
}

const loadStudents = async () => {
  const response = await getAllStudents()
  students.value = response.data
}

const loadCourses = async () => {
  const response = await getAllCourses()
  courses.value = response.data
}

const handleSearch = async () => {
  if (!searchKeyword.value) {
    await loadGrades()
    return
  }
  const response = await getAllGrades()
  grades.value = response.data.filter(g => 
    g.studentName.includes(searchKeyword.value) ||
    g.courseName.includes(searchKeyword.value)
  )
}

const openAddModal = () => {
  isEdit.value = false
  form.value = {
    id: null,
    studentId: '',
    courseId: '',
    score: 0,
    semester: '',
    examType: '期末考试'
  }
  modalVisible.value = true
}

const openEditModal = (row) => {
  isEdit.value = true
  form.value = {
    id: row.id,
    studentId: row.studentId,
    courseId: row.courseId,
    score: row.score,
    semester: row.semester,
    examType: row.examType
  }
  modalVisible.value = true
}

const handleSave = async () => {
  try {
    const gradeData = {
      studentId: form.value.studentId,
      courseId: form.value.courseId,
      score: form.value.score,
      semester: form.value.semester,
      examType: form.value.examType
    }
    if (isEdit.value) {
      gradeData.id = form.value.id
      await updateGrade(gradeData)
    } else {
      await addGrade(gradeData)
    }
    modalVisible.value = false
    await loadGrades()
    alert('保存成功')
  } catch (error) {
    alert('保存失败')
  }
}

const handleDelete = async (id) => {
  if (confirm('确定要删除该成绩吗？')) {
    try {
      await deleteGrade(id)
      await loadGrades()
      alert('删除成功')
    } catch (error) {
      alert('删除失败')
    }
  }
}
</script>

<style scoped>
.grades {
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
