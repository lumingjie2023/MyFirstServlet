<template>
  <div class="home">
    <div class="stats-cards">
      <div class="stat-card">
        <div class="stat-icon students-icon">👥</div>
        <div class="stat-info">
          <p class="stat-value">{{ studentCount }}</p>
          <p class="stat-label">学生总数</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon courses-icon">📚</div>
        <div class="stat-info">
          <p class="stat-value">{{ courseCount }}</p>
          <p class="stat-label">课程总数</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon grades-icon">📊</div>
        <div class="stat-info">
          <p class="stat-value">{{ gradeCount }}</p>
          <p class="stat-label">成绩记录</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon gpa-icon">🎯</div>
        <div class="stat-info">
          <p class="stat-value">{{ avgGPA.toFixed(2) }}</p>
          <p class="stat-label">平均绩点</p>
        </div>
      </div>
    </div>

    <div class="charts-row">
      <div class="chart-card">
        <h3>成绩分布</h3>
        <div class="chart-placeholder">
          <div class="bar-chart">
            <div class="bar" style="height: 60%;"><span>A</span></div>
            <div class="bar" style="height: 90%;"><span>B</span></div>
            <div class="bar" style="height: 40%;"><span>C</span></div>
            <div class="bar" style="height: 20%;"><span>D</span></div>
            <div class="bar" style="height: 10%;"><span>F</span></div>
          </div>
        </div>
      </div>
      <div class="chart-card">
        <h3>专业分布</h3>
        <div class="chart-placeholder">
          <div class="major-list">
            <div class="major-item"><span>计算机科学与技术</span><span class="count">3人</span></div>
            <div class="major-item"><span>软件工程</span><span class="count">2人</span></div>
            <div class="major-item"><span>人工智能</span><span class="count">2人</span></div>
            <div class="major-item"><span>网络工程</span><span class="count">1人</span></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '../utils/axios'

const studentCount = ref(0)
const courseCount = ref(0)
const gradeCount = ref(0)
const avgGPA = ref(0)

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    const token = localStorage.getItem('token')
    const headers = { Authorization: `Bearer ${token}` }

    const [studentsRes, coursesRes, gradesRes] = await Promise.all([
      axios.get('/student/all', { headers }),
      axios.get('/course/all', { headers }),
      axios.get('/grade/all', { headers })
    ])

    studentCount.value = studentsRes.data.length || 0
    courseCount.value = coursesRes.data.length || 0
    gradeCount.value = gradesRes.data.length || 0

    const studentIds = studentsRes.data.map(s => s.studentId)
    let totalGPA = 0
    let count = 0
    for (const id of studentIds) {
      try {
        const gpaRes = await axios.get(`/grade/gpa/${id}`, { headers })
        totalGPA += gpaRes.data || 0
        count++
      } catch (e) {
        continue
      }
    }
    avgGPA.value = count > 0 ? totalGPA / count : 0
  } catch (error) {
    console.error('加载数据失败:', error)
  }
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
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
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
}

.courses-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.grades-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.gpa-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
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
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.chart-card h3 {
  margin-bottom: 20px;
  font-size: 16px;
}

.chart-placeholder {
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.bar-chart {
  display: flex;
  align-items: flex-end;
  gap: 20px;
  height: 100%;
  padding: 20px;
}

.bar {
  width: 50px;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  border-radius: 4px 4px 0 0;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  padding-bottom: 10px;
  color: black;
  font-weight: bold;
}

.major-list {
  width: 100%;
}

.major-item {
  display: flex;
  justify-content: space-between;
  padding: 10px 20px;
  border-bottom: 1px solid #eee;
}

.major-item:last-child {
  border-bottom: none;
}

.major-item .count {
  color: #409eff;
  font-weight: bold;
}
</style>
