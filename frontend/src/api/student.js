
import axios from '../utils/axios'

export const getAllStudents = () => {
  return axios.get('/student/all')
}

export const getStudentById = (id) => {
  return axios.get(`/student/${id}`)
}

export const searchStudents = (keyword) => {
  return axios.get(`/student/search?keyword=${keyword}`)
}

export const getStudentGrades = (id) => {
  return axios.get(`/student/${id}/grades`)
}

export const getStudentsByMajor = (major) => {
  return axios.get(`/student/major/${major}`)
}

export const getStudentsByClass = (className) => {
  return axios.get(`/student/class/${className}`)
}

export const addStudent = (data) => {
  return axios.post('/student/add', data)
}

export const updateStudent = (data) => {
  return axios.put('/student/update', data)
}

export const deleteStudent = (id) => {
  return axios.delete(`/student/delete/${id}`)
}
