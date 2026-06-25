
import axios from '../utils/axios'

export const getAllGrades = () => {
  return axios.get('/grade/all')
}

export const getGradeById = (id) => {
  return axios.get(`/grade/${id}`)
}

export const getGradesByStudent = (studentId) => {
  return axios.get(`/grade/student/${studentId}`)
}

export const getGradesByCourse = (courseId) => {
  return axios.get(`/grade/course/${courseId}`)
}

export const getGradesBySemester = (semester) => {
  return axios.get(`/grade/semester/${semester}`)
}

export const getGPA = (studentId) => {
  return axios.get(`/grade/gpa/${studentId}`)
}

export const addGrade = (data) => {
  return axios.post('/grade/add', data)
}

export const updateGrade = (data) => {
  return axios.put('/grade/update', data)
}

export const deleteGrade = (id) => {
  return axios.delete(`/grade/delete/${id}`)
}
