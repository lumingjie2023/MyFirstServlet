<template>
  <div class="teacher-course-page">
    <h2>教师课程分配管理</h2>
    
    <div class="assign-section">
      <h3>分配课程给教师</h3>
      <div class="form-row">
        <select v-model="selectedTeacher" class="select-input">
          <option value="">选择教师</option>
          <option v-for="teacher in teachers" :key="teacher.username" :value="teacher.username">
            {{ teacher.realName }} ({{ teacher.username }})
          </option>
        </select>
        
        <select v-model="selectedCourse" class="select-input">
          <option value="">选择课程</option>
          <option v-for="course in courses" :key="course.courseId" :value="course.courseId">
            {{ course.courseName }} ({{ course.courseId }})
          </option>
        </select>
        
        <button class="assign-btn" @click="handleAssign" :disabled="!selectedTeacher || !selectedCourse">
          分配
        </button>
      </div>
    </div>
    
    <div class="list-section">
      <h3>当前分配列表</h3>
      <table class="data-table">
        <thead>
          <tr>
            <th>教师姓名</th>
            <th>教师用户名</th>
            <th>课程名称</th>
            <th>课程ID</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in teacherCourseList" :key="item.id">
            <td>{{ getTeacherName(item.teacherUsername) }}</td>
            <td>{{ item.teacherUsername }}</td>
            <td>{{ getCourseName(item.courseId) }}</td>
            <td>{{ item.courseId }}</td>
            <td>
              <button class="remove-btn" @click="handleRemove(item.teacherUsername, item.courseId)">
                移除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="teacherCourseList.length === 0" class="empty-msg">暂无分配记录</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAllTeacherCourses, assignCourseToTeacher, removeCourseFromTeacher } from '../api/teacherCourse'
import { getAllUsers } from '../api/user'
import axios from '../utils/axios'

const teachers = ref([])
const courses = ref([])
const teacherCourseList = ref([])
const selectedTeacher = ref('')
const selectedCourse = ref('')

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    const token = localStorage.getItem('token')
    const headers = { Authorization: `Bearer ${token}` }
    
    // 获取所有用户（筛选教师）
    const usersRes = await getAllUsers()
    teachers.value = usersRes.data.filter(u => u.role === 'TEACHER')
    
    // 获取所有课程
    const coursesRes = await axios.get('/course/all', { headers })
    courses.value = coursesRes.data
    
    // 获取教师-课程关联
    const tcRes = await getAllTeacherCourses()
    teacherCourseList.value = tcRes.data
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const getTeacherName = (username) => {
  const teacher = teachers.value.find(t => t.username === username)
  return teacher ? teacher.realName : '未知'
}

const getCourseName = (courseId) => {
  const course = courses.value.find(c => c.courseId === courseId)
  return course ? course.courseName : '未知'
}

const handleAssign = async () => {
  if (!selectedTeacher.value || !selectedCourse.value) {
    alert('请选择教师和课程')
    return
  }
  
  try {
    await assignCourseToTeacher(selectedTeacher.value, selectedCourse.value)
    alert('分配成功')
    selectedTeacher.value = ''
    selectedCourse.value = ''
    await loadData()
  } catch (error) {
    console.error('分配失败:', error)
    alert('分配失败')
  }
}

const handleRemove = async (teacherUsername, courseId) => {
  if (!confirm('确定要移除该分配吗？')) {
    return
  }
  
  try {
    await removeCourseFromTeacher(teacherUsername, courseId)
    alert('移除成功')
    await loadData()
  } catch (error) {
    console.error('移除失败:', error)
    alert('移除失败')
  }
}
</script>

<style scoped>
.teacher-course-page {
  padding: 20px;
}

.teacher-course-page h2 {
  margin-bottom: 20px;
  color: #333;
}

.assign-section,
.list-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.assign-section h3,
.list-section h3 {
  margin-bottom: 15px;
  color: #333;
}

.form-row {
  display: flex;
  gap: 15px;
  align-items: center;
}

.select-input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  min-width: 200px;
}

.assign-btn {
  padding: 10px 20px;
  background: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.assign-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.assign-btn:hover:not(:disabled) {
  background: #66b1ff;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
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
  color: #333;
}

.remove-btn {
  padding: 5px 15px;
  background: #f56c6c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.remove-btn:hover {
  background: #f78989;
}

.empty-msg {
  text-align: center;
  color: #999;
  padding: 20px;
}
</style>