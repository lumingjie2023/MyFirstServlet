import axios from '../utils/axios'

export const getAllUsers = () => {
  return axios.get('/user/all')
}

export const getUserByUsername = (username) => {
  return axios.get(`/user/${username}`)
}

export const addUser = (data) => {
  return axios.post('/user/add', data)
}

export const updateUser = (data) => {
  return axios.put('/user/update', data)
}

export const deleteUser = (username) => {
  return axios.delete(`/user/delete/${username}`)
}
