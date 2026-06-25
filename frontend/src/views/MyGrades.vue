<template>
  <div class="my-grades">
    <div class="gpa-card">
      <div class="gpa-info">
        <p class="gpa-label">我的绩点</p>
        <p class="gpa-value">{{ gpa.toFixed(2) }}</p>
      </div>
    </div>
    
    <div class="grades-section">
      <h3>我的成绩</h3>
      <table class="grades-table">
        <thead>
          <tr>
            <th>课程名称</th>
            <th>成绩</th>
            <th>等级</th>
            <th>学分</th>
            <th>学期</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="grade in grades" :key="grade.id">
            <td>{{ grade.courseName }}</td>
            <td>{{ grade.score }}</td>
            <td><span :class="['grade-tag', getGradeClass(grade.gradeLevel)]">{{ grade.gradeLevel }}</span></td>
            <td>{{ grade.credit }}</td>
            <td>{{ grade.semester }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    
    <div class="statistics-section">
      <h3>成绩统计</h3>
      <div class="stats-grid">
        <div class="stat-item">
          <p class="stat-value">{{ totalCredits }}</p>
          <p class="stat-label">总学分</p>
        </div>
        <div class="stat-item">
          <p class="stat-value">{{ passedCourses }}</p>
          <p class="stat-label">通过课程</p>
        </div>
        <div class="stat-item">
          <p class="stat-value">{{ avgScore.toFixed(1) }}</p>
          <p class="stat-label">平均分</p>
        </div>
        <div class="stat-item">
          <p class="stat-value">{{ maxGradeCount }}个{{ highestGrade }}</p>
          <p class="stat-label">最高等级</p>
        </div>
      </div>
      
      <h4>成绩等级分布</h4>
      <div class="distribution-chart">
        <div class="chart-bars">
          <div class="bar-group">
            <div class="bar-wrapper">
              <span class="bar-value">{{ gradeDistribution.A }}</span>
              <div class="bar-fill a" :style="{ height: getBarHeight('A') + '%' }"></div>
            </div>
            <span class="bar-label">A</span>
          </div>
          <div class="bar-group">
            <div class="bar-wrapper">
              <span class="bar-value">{{ gradeDistribution.B }}</span>
              <div class="bar-fill b" :style="{ height: getBarHeight('B') + '%' }"></div>
            </div>
            <span class="bar-label">B</span>
          </div>
          <div class="bar-group">
            <div class="bar-wrapper">
              <span class="bar-value">{{ gradeDistribution.C }}</span>
              <div class="bar-fill c" :style="{ height: getBarHeight('C') + '%' }"></div>
            </div>
            <span class="bar-label">C</span>
          </div>
          <div class="bar-group">
            <div class="bar-wrapper">
              <span class="bar-value">{{ gradeDistribution.D }}</span>
              <div class="bar-fill d" :style="{ height: getBarHeight('D') + '%' }"></div>
            </div>
            <span class="bar-label">D</span>
          </div>
          <div class="bar-group">
            <div class="bar-wrapper">
              <span class="bar-value">{{ gradeDistribution.F }}</span>
              <div class="bar-fill f" :style="{ height: getBarHeight('F') + '%' }"></div>
            </div>
            <span class="bar-label">F</span>
          </div>
        </div>
        <div class="chart-base-line"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getGradesByStudent, getGPA } from '../api/grade'

const user = JSON.parse(localStorage.getItem('user') || '{}')
const studentId = user.username || ''

const grades = ref([])
const gpa = ref(0)

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    const [gradesResponse, gpaResponse] = await Promise.all([
      getGradesByStudent(studentId),
      getGPA(studentId)
    ])
    grades.value = gradesResponse.data || []
    gpa.value = gpaResponse.data || 0
  } catch (error) {
    console.error('加载成绩数据失败:', error)
    grades.value = []
    gpa.value = 0
  }
}

const totalCredits = computed(() => {
  return grades.value.reduce((sum, g) => sum + (g.credit || 0), 0)
})

const passedCourses = computed(() => {
  return grades.value.filter(g => g.score >= 60).length
})

const avgScore = computed(() => {
  const validGrades = grades.value.filter(g => g.score && g.score > 0)
  if (validGrades.length === 0) return 0
  return validGrades.reduce((sum, g) => sum + g.score, 0) / validGrades.length
})

const gradeDistribution = computed(() => {
  const dist = { A: 0, B: 0, C: 0, D: 0, F: 0 }
  grades.value.forEach(g => {
    const level = g.gradeLevel
    if (level && dist[level] !== undefined) {
      dist[level]++
    }
  })
  return dist
})

const maxGradeCount = computed(() => {
  const dist = gradeDistribution.value
  const levels = ['A', 'B', 'C', 'D', 'F']
  for (const level of levels) {
    if (dist[level] > 0) {
      return dist[level]
    }
  }
  return 0
})

const highestGrade = computed(() => {
  const dist = gradeDistribution.value
  const levels = ['A', 'B', 'C', 'D', 'F']
  for (const level of levels) {
    if (dist[level] > 0) {
      return level
    }
  }
  return '-'
})

const getBarHeight = (level) => {
  const dist = gradeDistribution.value
  const maxCount = Math.max(dist.A, dist.B, dist.C, dist.D, dist.F, 1)
  return (dist[level] / maxCount) * 100
}

const getGradeClass = (level) => {
  return `grade-${(level || '').toLowerCase()}`
}
</script>

<style scoped>
.my-grades {
  padding: 20px;
}

.gpa-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 30px;
  margin-bottom: 20px;
  color: white;
}

.gpa-info {
  text-align: center;
}

.gpa-label {
  font-size: 16px;
  margin: 0 0 10px 0;
  opacity: 0.9;
}

.gpa-value {
  font-size: 48px;
  font-weight: bold;
  margin: 0;
}

.grades-section,
.statistics-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
}

.grades-section h3,
.statistics-section h3 {
  margin: 0 0 15px 0;
  font-size: 16px;
  color: #333;
}

.statistics-section h4 {
  margin: 20px 0 15px 0;
  font-size: 14px;
  color: #333;
}

.grades-table {
  width: 100%;
  border-collapse: collapse;
}

.grades-table th,
.grades-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.grades-table th {
  background: #f5f7fa;
  font-weight: bold;
}

.grade-tag {
  padding: 4px 8px;
  border-radius: 4px;
  font-weight: bold;
  font-size: 12px;
}

.grade-a { background: #f0f9eb; color: #67c23a; }
.grade-b { background: #f0f9eb; color: #67c23a; }
.grade-c { background: #fdf6ec; color: #e6a23c; }
.grade-d { background: #fef0f0; color: #f56c6c; }
.grade-f { background: #fef0f0; color: #f56c6c; }
.grade- { background: #f5f5f5; color: #999; }

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  margin: 0 0 5px 0;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.distribution-chart {
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.chart-bars {
  display: flex;
  align-items: flex-end;
  justify-content: center;
  gap: 50px;
  height: 180px;
}

.bar-group {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 50px;
}

.bar-wrapper {
  position: relative;
  width: 100%;
  height: 150px;
  display: flex;
  align-items: flex-end;
  justify-content: center;
}

.bar-value {
  position: absolute;
  top: -22px;
  font-size: 14px;
  font-weight: bold;
  color: #666;
}

.bar-fill {
  width: 45px;
  border-radius: 4px 4px 0 0;
  transition: height 0.3s;
  min-height: 4px;
}

.bar-fill.a { background: linear-gradient(180deg, #4CAF50 0%, #388E3C 100%); }
.bar-fill.b { background: linear-gradient(180deg, #8BC34A 0%, #689F38 100%); }
.bar-fill.c { background: linear-gradient(180deg, #FFC107 0%, #FFA000 100%); }
.bar-fill.d { background: linear-gradient(180deg, #FF9800 0%, #F57C00 100%); }
.bar-fill.f { background: linear-gradient(180deg, #F44336 0%, #D32F2F 100%); }

.bar-label {
  margin-top: 10px;
  font-weight: bold;
  color: #333;
  font-size: 14px;
}

.chart-base-line {
  width: 100%;
  height: 2px;
  background: #ddd;
  margin-top: 5px;
}
</style>
