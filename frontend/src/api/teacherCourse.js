import axios from '../utils/axios'

// 获取教师负责的所有课程
export const getTeacherCourses = (username) => {
  return axios.get(`/teacher-course/teacher/${username}`)
}

// 为教师分配课程
export const assignCourseToTeacher = (teacherUsername, courseId) => {
  return axios.post('/teacher-course/assign', {
    teacherUsername,
    courseId
  })
}

// 移除教师的课程
export const removeCourseFromTeacher = (teacherUsername, courseId) => {
  return axios.post('/teacher-course/remove', {
    teacherUsername,
    courseId
  })
}

// 获取所有教师-课程关联
export const getAllTeacherCourses = () => {
  return axios.get('/teacher-course/all')
}