
<template>
  <div class="home">
    <div class="stats-cards">
      <el-card class="stat-card">
        <div class="stat-icon students-icon">
          <el-icon><component :is="Users" /></el-icon>
        </div>
        <div class="stat-info">
          <p class="stat-value">{{ studentCount }}</p>
          <p class="stat-label">学生总数</p>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-icon courses-icon">
          <el-icon><component :is="BookOpen" /></el-icon>
        </div>
        <div class="stat-info">
          <p class="stat-value">{{ courseCount }}</p>
          <p class="stat-label">课程总数</p>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-icon grades-icon">
          <el-icon><component :is="BarChart" /></el-icon>
        </div>
        <div class="stat-info">
          <p class="stat-value">{{ gradeCount }}</p>
          <p class="stat-label">成绩记录</p>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-icon gpa-icon">
          <el-icon><component :is="TrendingUp" /></el-icon>
        </div>
        <div class="stat-info">
          <p class="stat-value">{{ avgGPA.toFixed(2) }}</p>
          <p class="stat-label">平均绩点</p>
        </div>
      </el-card>
    </div>
    
    <div class="charts-row">
      <el-card class="chart-card">
        <h3>成绩分布</h3>
        <div ref="gradeChart" class="chart"></div>
      </el-card>
      <el-card class="chart-card">
        <h3>各专业人数</h3>
        <div ref="majorChart" class="chart"></div>
      </el-card>
    </div>
    
    <div class="recent-grades">
      <el-card>
        <h3>最新成绩记录</h3>
        <el-table :data="recentGrades" border>
          <el-table-column prop="studentName" label="学生姓名" />
          <el-table-column prop="courseName" label="课程名称" />
          <el-table-column prop="score" label="成绩" />
          <el-table-column prop="gradeLevel" label="等级" />
          <el-table-column prop="semester" label="学期" />
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Users, BookOpen, BarChart, TrendingUp } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getAllStudents } from '../api/student'
import { getAllCourses } from '../api/course'
import { getAllGrades, getGPA } from '../api/grade'

const studentCount = ref(0)
const courseCount = ref(0)
const gradeCount = ref(0)
const avgGPA = ref(0)
const recentGrades = ref([])
const gradeChart = ref(null)
const majorChart = ref(null)

onMounted(async () => {
  await loadData()
  initCharts()
})

const loadData = async () => {
  const [students, courses, grades] = await Promise.all([
    getAllStudents(),
    getAllCourses(),
    getAllGrades()
  ])
  
  studentCount.value = students.data.length
  courseCount.value = courses.data.length
  gradeCount.value = grades.data.length
  recentGrades.value = grades.data.slice(0, 10)
  
  const studentIds = students.data.map(s => s.studentId)
  let totalGPA = 0
  let count = 0
  for (const id of studentIds) {
    try {
      const gpa = await getGPA(id)
      totalGPA += gpa.data
      count++
    } catch (e) {
      continue
    }
  }
  avgGPA.value = count > 0 ? totalGPA / count : 0
}

const initCharts = () => {
  const gradeChartInstance = echarts.init(gradeChart.value)
  gradeChartInstance.setOption({
    title: { text: '成绩等级分布' },
    tooltip: {},
    legend: { data: ['人数'] },
    xAxis: {
      data: ['A', 'B', 'C', 'D', 'F']
    },
    yAxis: {},
    series: [{
      name: '人数',
      type: 'bar',
      data: [12, 18, 8, 4, 2]
    }]
  })
  
  const majorChartInstance = echarts.init(majorChart.value)
  majorChartInstance.setOption({
    title: { text: '专业分布' },
    tooltip: {},
    series: [{
      name: '专业',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: true
      },
      data: [
        { value: 3, name: '计算机科学与技术' },
        { value: 2, name: '软件工程' },
        { value: 2, name: '人工智能' },
        { value: 1, name: '网络工程' }
      ]
    }]
  })
  
  window.addEventListener('resize', () => {
    gradeChartInstance.resize()
    majorChartInstance.resize()
  })
}
</script>

<style scoped>
.home {
  padding: 20px;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.students-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.courses-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.grades-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.gpa-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  color: white;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  margin: 0;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin: 5px 0 0 0;
}

.charts-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.chart-card {
  height: 300px;
}

.chart-card h3 {
  margin-bottom: 10px;
  font-size: 16px;
}

.chart {
  width: 100%;
  height: calc(100% - 40px);
}

.recent-grades {
  margin-top: 20px;
}

.recent-grades h3 {
  margin-bottom: 10px;
  font-size: 16px;
}
</style>
