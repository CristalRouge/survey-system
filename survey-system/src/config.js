// API配置
const API_BASE_URL = 'http://localhost:8080/api'

// 演示模式开关 - 设为true使用假数据，设为false调用后端API
export const DEMO_MODE = true

// 获取API基础URL
export const getApiBaseUrl = () => API_BASE_URL

// 获取Token (从localStorage)
export const getToken = () => localStorage.getItem('survey_token')

// 设置Token
export const setToken = (token) => localStorage.setItem('survey_token', token)

// 移除Token
export const removeToken = () => localStorage.removeItem('survey_token')
