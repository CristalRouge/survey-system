import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '../api/index.js'
import { getToken, removeToken } from '../config.js'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const loading = ref(false)
  const error = ref(null)

  const isLoggedIn = computed(() => !!getToken())

  const login = async (username, password) => {
    loading.value = true
    error.value = null
    try {
      const res = await authApi.login(username, password)
      if (res.code === 200) {
        user.value = res.data.user
        return res.data
      } else {
        error.value = res.message
        throw new Error(res.message)
      }
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  const logout = async () => {
    try {
      await authApi.logout()
    } catch (err) {
      console.error('Logout error:', err)
    } finally {
      removeToken()
      user.value = null
    }
  }

  const fetchCurrentUser = async () => {
    if (!getToken()) return null
    try {
      const res = await authApi.getCurrentUser()
      if (res.code === 200) {
        user.value = res.data
      }
    } catch (err) {
      console.error('Failed to fetch user:', err)
      removeToken()
      user.value = null
    }
  }

  return {
    user, loading, error, isLoggedIn,
    login, logout, fetchCurrentUser
  }
})
