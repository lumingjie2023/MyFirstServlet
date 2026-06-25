
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Login',
    component: () => import('../views/LoginTest.vue')
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/Dashboard.vue'),
    children: [
      { path: 'home', name: 'Home', component: () => import('../views/HomeSimple.vue') },
      { path: 'students', name: 'Students', component: () => import('../views/Students.vue') },
      { path: 'courses', name: 'Courses', component: () => import('../views/Courses.vue') },
      { path: 'grades', name: 'Grades', component: () => import('../views/Grades.vue') },
      { path: 'mygrades', name: 'MyGrades', component: () => import('../views/MyGrades.vue') },
      { path: 'users', name: 'Users', component: () => import('../views/Users.vue') },
      { path: 'teacher-course', name: 'TeacherCourse', component: () => import('../views/TeacherCourse.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path !== '/' && !localStorage.getItem('token')) {
    next('/')
  } else {
    next()
  }
})

export default router
