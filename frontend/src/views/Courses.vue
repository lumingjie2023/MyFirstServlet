<template>
  <div class="courses">
    <div class="toolbar">
      <input v-model="searchKeyword" placeholder="搜索课程" class="search-input" @keyup.enter="handleSearch" />
      <button class="add-btn" @click="openAddModal">添加课程</button>
    </div>
    
    <table class="data-table">
      <thead>
        <tr>
          <th>课程编号</th>
          <th>课程名称</th>
          <th>学分</th>
          <th>授课教师</th>
          <th>开课院系</th>
          <th>学期</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="course in courses" :key="course.courseId">
          <td>{{ course.courseId }}</td>
          <td>{{ course.courseName }}</td>
          <td>{{ course.credit }}</td>
          <td>{{ course.teacherName }}</td>
          <td>{{ course.department }}</td>
          <td>{{ course.semester }}</td>
          <td>
            <button class="edit-btn" @click="openEditModal(course)">编辑</button>
            <button class="delete-btn" @click="handleDelete(course.courseId)">删除</button>
            <button class="view-btn" @click="viewGrades(course.courseId)">查看成绩</button>
          </td>
        </tr>
      </tbody>
    </table>
    
    <div v-if="modalVisible" class="modal-overlay" @click.self="modalVisible = false">
      <div class="modal-content">
        <h3>{{ isEdit ? '编辑课程' : '添加课程' }}</h3>
        <div class="form-group">
          <label>课程编号</label>
          <input v-model="form.courseId" :disabled="isEdit" />
        </div>
        <div class="form-group">
          <label>课程名称</label>
          <input v-model="form.courseName" />
        </div>
        <div class="form-group">
          <label>学分</label>
          <input type="number" v-model="form.credit" />
        </div>
        <div class="form-group">
          <label>授课教师</label>
          <input v-model="form.teacherName" />
        </div>
        <div class="form-group">
          <label>开课院系</label>
          <input v-model="form.department" />
        </div>
        <div class="form-group">
          <label>学期</label>
          <input v-model="form.semester" />
        </div>
        <div class="modal-footer">
          <button class="cancel-btn" @click="modalVisible = false">取消</button>
          <button class="save-btn" @click="handleSave">保存</button>
        </div>
      </div>
    </div>
    
    <div v-if="gradesModalVisible" class="modal-overlay" @click.self="gradesModalVisible = false">
      <div class="modal-content grades-modal">
        <h3>课程成绩</h3>
        <table class="data-table small">
          <thead>
            <tr>
              <th>学号</th>
              <th>学生姓名</th>
              <th>成绩</th>
              <th>等级</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="grade in courseGrades" :key="grade.id">
              <td>{{ grade.studentId }}</td>
              <td>{{ grade.studentName }}</td>
              <td>{{ grade.score }}</td>
              <td>{{ grade.gradeLevel }}</td>
            </tr>
          </tbody>
        </table>
        <div class="modal-footer">
          <button class="cancel-btn" @click="gradesModalVisible = false">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAllCourses, addCourse, updateCourse, deleteCourse, getCourseGrades } from '../api/course'

const courses = ref([])
const searchKeyword = ref('')
const modalVisible = ref(false)
const gradesModalVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  courseId: '',
  courseName: '',
  credit: 3.0,
  teacherName: '',
  department: '',
  semester: ''
})
const courseGrades = ref([])

onMounted(async () => {
  await loadCourses()
})

const loadCourses = async () => {
  const response = await getAllCourses()
  courses.value = response.data
}

const handleSearch = async () => {
  if (!searchKeyword.value) {
    await loadCourses()
    return
  }
  const response = await getAllCourses()
  courses.value = response.data.filter(c => 
    c.courseId.includes(searchKeyword.value) ||
    c.courseName.includes(searchKeyword.value) ||
    c.teacherName.includes(searchKeyword.value) ||
    c.department.includes(searchKeyword.value)
  )
}

const openAddModal = () => {
  isEdit.value = false
  form.value = {
    courseId: '',
    courseName: '',
    credit: 3.0,
    teacherName: '',
    department: '',
    semester: ''
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
      await updateCourse(form.value)
    } else {
      await addCourse(form.value)
    }
    modalVisible.value = false
    await loadCourses()
    alert('保存成功')
  } catch (error) {
    alert('保存失败')
  }
}

const handleDelete = async (id) => {
  if (confirm('确定要删除该课程吗？')) {
    try {
      await deleteCourse(id)
      await loadCourses()
      alert('删除成功')
    } catch (error) {
      alert('删除失败')
    }
  }
}

const viewGrades = async (courseId) => {
  const response = await getCourseGrades(courseId)
  courseGrades.value = response.data
  gradesModalVisible.value = true
}
</script>

<style scoped>
.courses {
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

.data-table.small {
  font-size: 14px;
}

.data-table th,
.data-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.data-table.small th,
.data-table.small td {
  padding: 8px;
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
  margin-right: 5px;
}

.delete-btn:hover {
  background: #f78989;
}

.view-btn {
  padding: 4px 8px;
  background: #909399;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.view-btn:hover {
  background: #a6a9ad;
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
  max-height: 90vh;
  overflow-y: auto;
}

.modal-content.grades-modal {
  width: 600px;
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

.form-group input {
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
