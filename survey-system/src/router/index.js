import { createRouter, createWebHistory } from 'vue-router'
const routes = [
  { path: '/', name: 'Home', component: () => import('../views/HomePage.vue') },
  { path: '/survey/:id', name: 'SurveyFill', component: () => import('../views/SurveyFill.vue') },
  { path: '/survey/success', name: 'SurveySuccess', component: () => import('../views/SurveySuccess.vue') },
  { path: '/admin', name: 'AdminDashboard', component: () => import('../views/AdminDashboard.vue') },
  { path: '/admin/survey/create', name: 'SurveyCreate', component: () => import('../views/SurveyEditor.vue') },
  { path: '/admin/survey/:id', name: 'SurveyEditor', component: () => import('../views/SurveyEditor.vue') },
  { path: '/admin/responses/:id', name: 'ResponseStats', component: () => import('../views/ResponseStats.vue') }
]
const router = createRouter({
  history: createWebHistory(),
  routes
})
export default router
