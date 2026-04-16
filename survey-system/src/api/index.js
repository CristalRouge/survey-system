/**
 * API服务层
 * 支持演示模式和API模式切换
 */
import { DEMO_MODE, getApiBaseUrl, getToken, setToken, removeToken } from '../config.js'

const BASE_URL = getApiBaseUrl()

// 通用请求封装
const request = async (url, options = {}) => {
  const token = getToken()
  const headers = {
    'Content-Type': 'application/json',
    ...(token && { 'Authorization': `Bearer ${token}` }),
    ...options.headers
  }

  // 文件上传使用FormData
  if (options.body instanceof FormData) {
    delete headers['Content-Type']
  }

  try {
    const response = await fetch(`${BASE_URL}${url}`, {
      ...options,
      headers,
      body: options.body instanceof FormData ? options.body :
            options.body ? JSON.stringify(options.body) : undefined
    })

    const data = await response.json()

    if (!response.ok) {
      throw new Error(data.message || '请求失败')
    }

    // 如果返回token则保存
    if (data.data?.token) {
      setToken(data.data.token)
    }

    return data
  } catch (error) {
    console.error('API请求错误:', error)
    throw error
  }
}

// 认证API
export const authApi = {
  login: async (username, password) => {
    if (DEMO_MODE) {
      // 演示模式：模拟登录成功
      const mockToken = 'demo_token_' + Date.now()
      setToken(mockToken)
      return {
        code: 200,
        message: 'success',
        data: {
          token: mockToken,
          refreshToken: 'refresh_' + mockToken,
          expiresIn: 86400000,
          user: {
            id: 1,
            username: username,
            nickname: username,
            role: 'admin',
            status: 1
          }
        }
      }
    }
    return request('/auth/login', {
      method: 'POST',
      body: { username, password }
    })
  },

  register: async (data) => {
    if (DEMO_MODE) {
      return { code: 200, message: 'success', data: null }
    }
    return request('/auth/register', {
      method: 'POST',
      body: data
    })
  },

  logout: async () => {
    removeToken()
    return { code: 200, message: 'success' }
  },

  getCurrentUser: async () => {
    if (DEMO_MODE) {
      return {
        code: 200,
        data: {
          id: 1,
          username: 'admin',
          nickname: '管理员',
          role: 'admin',
          status: 1
        }
      }
    }
    return request('/auth/me')
  }
}

// 问卷API
export const surveyApi = {
  // 获取问卷列表
  getSurveys: async (params = {}) => {
    if (DEMO_MODE) {
      const { default: surveys } = await import('../data/surveys.json')
      return { code: 200, data: surveys }
    }
    const queryString = new URLSearchParams(params).toString()
    return request(`/surveys${queryString ? '?' + queryString : ''}`)
  },

  // 获取已发布问卷
  getPublishedSurveys: async () => {
    if (DEMO_MODE) {
      const { default: surveys } = await import('../data/surveys.json')
      return { code: 200, data: surveys.filter(s => s.status === 'published') }
    }
    return request('/surveys/published')
  },

  // 获取问卷详情
  getSurveyById: async (id) => {
    if (DEMO_MODE) {
      const { default: surveys } = await import('../data/surveys.json')
      const survey = surveys.find(s => s.id === id)
      if (!survey) throw new Error('问卷不存在')
      return { code: 200, data: survey }
    }
    return request(`/surveys/${id}`)
  },

  // 创建问卷
  createSurvey: async (data) => {
    if (DEMO_MODE) {
      const newSurvey = {
        ...data,
        id: Date.now(),
        status: 'draft',
        responseCount: 0,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
      return { code: 200, data: newSurvey }
    }
    return request('/surveys', {
      method: 'POST',
      body: data
    })
  },

  // 更新问卷
  updateSurvey: async (id, data) => {
    if (DEMO_MODE) {
      return { code: 200, data: { ...data, id, updatedAt: new Date().toISOString() } }
    }
    return request(`/surveys/${id}`, {
      method: 'PUT',
      body: data
    })
  },

  // 删除问卷
  deleteSurvey: async (id) => {
    if (DEMO_MODE) {
      return { code: 200, message: '删除成功' }
    }
    return request(`/surveys/${id}`, { method: 'DELETE' })
  },

  // 更新问卷状态
  updateStatus: async (id, status) => {
    if (DEMO_MODE) {
      return { code: 200, message: status === 'published' ? '发布成功' : '关闭成功' }
    }
    return request(`/surveys/${id}/status?status=${status}`, { method: 'PATCH' })
  },

  // 获取问卷统计
  getSurveyStats: async (id) => {
    if (DEMO_MODE) {
      return {
        code: 200,
        data: {
          surveyId: id,
          totalResponses: Math.floor(Math.random() * 100) + 50,
          todayResponses: Math.floor(Math.random() * 20) + 5,
          completionRate: 0.85
        }
      }
    }
    return request(`/surveys/${id}/stats`)
  }
}

// 公开问卷API (无需认证)
export const publicSurveyApi = {
  // 获取公开问卷
  getSurvey: async (id) => {
    if (DEMO_MODE) {
      const { default: surveys } = await import('../data/surveys.json')
      const survey = surveys.find(s => s.id === id && s.status === 'published')
      if (!survey) throw new Error('问卷不存在或未发布')
      return { code: 200, data: survey }
    }
    return request(`/public/surveys/${id}`)
  },

  // 提交问卷回答
  submitResponse: async (surveyId, answers) => {
    if (DEMO_MODE) {
      // 模拟提交延迟
      await new Promise(resolve => setTimeout(resolve, 500))
      return { code: 200, message: '提交成功' }
    }
    return request(`/public/surveys/${surveyId}/responses`, {
      method: 'POST',
      body: { answers }
    })
  }
}

// 文件上传API
export const fileApi = {
  upload: async (file) => {
    if (DEMO_MODE) {
      // 演示模式：返回假图片URL
      await new Promise(resolve => setTimeout(resolve, 300))
      const fakeUrl = `https://picsum.photos/seed/${Date.now()}/800/600`
      return { code: 200, data: fakeUrl }
    }

    const formData = new FormData()
    formData.append('file', file)

    const token = getToken()
    const response = await fetch(`${BASE_URL}/files/upload`, {
      method: 'POST',
      headers: {
        ...(token && { 'Authorization': `Bearer ${token}` })
      },
      body: formData
    })

    const data = await response.json()
    return data
  },

  uploadMultiple: async (files) => {
    if (DEMO_MODE) {
      await new Promise(resolve => setTimeout(resolve, 500))
      return {
        code: 200,
        data: files.map(() => `https://picsum.photos/seed/${Date.now()}/800/600`)
      }
    }

    const formData = new FormData()
    files.forEach(file => formData.append('files', file))

    const token = getToken()
    const response = await fetch(`${BASE_URL}/files/upload/multiple`, {
      method: 'POST',
      headers: {
        ...(token && { 'Authorization': `Bearer ${token}` })
      },
      body: formData
    })

    return response.json()
  }
}

export default {
  auth: authApi,
  survey: surveyApi,
  publicSurvey: publicSurveyApi,
  file: fileApi
}
