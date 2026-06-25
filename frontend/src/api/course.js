
import axios from '../utils/axios'

export const getAllCourses = () => {
  return axios.get('/course/all')
}

export const getCourseById = (id) => {
  return axios.get(`/course/${id}`)
}

export const searchCourses = (keyword) => {
  return axios.get(`/course/search?keyword=${keyword}`)
}

export const getCourseGrades = (id) => {
  return axios.get(`/course/${id}/grades`)
}

export const getCoursesByDepartment = (department) => {
  return axios.get(`/course/department/${department}`)
}

export const getCoursesBySemester = (semester) => {
  return axios.get(`/course/semester/${semester}`)
}

export const addCourse = (data) => {
  return axios.post('/course/add', data)
}

export const updateCourse = (data) => {
  return axios.put('/course/update', data)
}

export const deleteCourse = (id) => {
  return axios.delete(`/course/delete/${id}`)
}
