import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { surveyApi, publicSurveyApi } from '../api/index.js'
import surveyData from '../data/surveys.json'

export const useSurveyStore = defineStore('survey', () => {
  const surveys = ref([])
  const responses = ref([])
  const loading = ref(false)
  const error = ref(null)

  // 从本地JSON加载数据
  const loadFromLocal = () => {
    surveys.value = surveyData.surveys || []
    responses.value = surveyData.responses || []
  }

  // 从API加载数据
  const loadFromApi = async () => {
    try {
      loading.value = true
      error.value = null
      const res = await surveyApi.getSurveys()
      if (res.code === 200) {
        surveys.value = res.data
      }
    } catch (err) {
      error.value = err.message
      console.error('Failed to load surveys from API:', err)
      // 降级到本地数据
      loadFromLocal()
    } finally {
      loading.value = false
    }
  }

  const loadSurveys = async () => {
    await loadFromApi()
  }

  const getSurveyById = (id) => {
    // 优先从本地数据查找
    let survey = surveys.value.find(s => s.id === id)
    if (survey) return survey

    // 如果本地没有且非演示模式，尝试从API获取
    if (!survey) {
      survey = surveyData.surveys?.find(s => s.id === id)
    }
    return survey
  }

  const getPublishedSurvey = async (id) => {
    try {
      const res = await publicSurveyApi.getSurvey(id)
      if (res.code === 200) {
        return res.data
      }
    } catch (err) {
      console.error('Failed to get published survey:', err)
    }
    return getSurveyById(id)
  }

  const createSurvey = async (survey) => {
    try {
      const res = await surveyApi.createSurvey(survey)
      if (res.code === 200) {
        surveys.value.unshift(res.data)
        return res.data
      }
    } catch (err) {
      console.error('Failed to create survey:', err)
      // 本地创建
      const newSurvey = {
        ...survey,
        id: `survey-${Date.now()}`,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        responseCount: 0
      }
      surveys.value.unshift(newSurvey)
      return newSurvey
    }
    return null
  }

  const updateSurvey = async (id, updates) => {
    try {
      const res = await surveyApi.updateSurvey(id, updates)
      if (res.code === 200) {
        const index = surveys.value.findIndex(s => s.id === id)
        if (index !== -1) {
          surveys.value[index] = { ...surveys.value[index], ...res.data }
        }
        return res.data
      }
    } catch (err) {
      console.error('Failed to update survey:', err)
      // 本地更新
      const index = surveys.value.findIndex(s => s.id === id)
      if (index !== -1) {
        surveys.value[index] = { ...surveys.value[index], ...updates, updatedAt: new Date().toISOString() }
        return surveys.value[index]
      }
    }
    return null
  }

  const deleteSurvey = async (id) => {
    try {
      await surveyApi.deleteSurvey(id)
      const index = surveys.value.findIndex(s => s.id === id)
      if (index !== -1) {
        surveys.value.splice(index, 1)
      }
      return true
    } catch (err) {
      console.error('Failed to delete survey:', err)
      // 本地删除
      const index = surveys.value.findIndex(s => s.id === id)
      if (index !== -1) {
        surveys.value.splice(index, 1)
        return true
      }
      return false
    }
  }

  const submitResponse = async (surveyId, answers) => {
    try {
      const res = await publicSurveyApi.submitResponse(surveyId, answers)
      if (res.code === 200) {
        const response = {
          id: `resp-${Date.now()}`,
          surveyId,
          answers,
          submittedAt: new Date().toISOString()
        }
        responses.value.push(response)
        const survey = surveys.value.find(s => s.id === surveyId)
        if (survey) survey.responseCount = (survey.responseCount || 0) + 1
        return response
      }
    } catch (err) {
      console.error('Failed to submit response:', err)
      // 本地提交
      const response = {
        id: `resp-${Date.now()}`,
        surveyId,
        answers,
        submittedAt: new Date().toISOString()
      }
      responses.value.push(response)
      const survey = surveys.value.find(s => s.id === surveyId)
      if (survey) survey.responseCount = (survey.responseCount || 0) + 1
      return response
    }
    return null
  }

  const getSurveyResponses = (surveyId) => responses.value.filter(r => r.surveyId === surveyId)

  const updateSurveyStatus = async (id, status) => {
    try {
      await surveyApi.updateStatus(id, status)
      await updateSurvey(id, { status })
      return true
    } catch (err) {
      console.error('Failed to update status:', err)
      return updateSurvey(id, { status }) !== null
    }
  }

  return {
    surveys, responses, loading, error,
    loadSurveys, getSurveyById, getPublishedSurvey, createSurvey, updateSurvey, deleteSurvey,
    submitResponse, getSurveyResponses, updateSurveyStatus
  }
})
