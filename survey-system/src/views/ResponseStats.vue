<template>
  <div class="min-h-screen bg-gray-50">
    <header class="bg-white shadow-sm sticky top-0 z-50">
      <div class="max-w-7xl mx-auto px-4 py-4">
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-3">
            <button @click="goBack" class="p-2 hover:bg-gray-100 rounded-lg transition-colors">
              <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
              </svg>
            </button>
            <div>
              <h1 class="text-xl font-bold text-gray-800">回答统计</h1>
              <p class="text-sm text-gray-600">{{ survey?.title }}</p>
            </div>
          </div>
          <el-button @click="exportData">
            <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
            导出数据
          </el-button>
        </div>
      </div>
    </header>

    <main class="max-w-7xl mx-auto px-4 py-6">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div class="bg-white rounded-lg p-6 shadow-sm">
          <div class="flex items-center gap-4">
            <div class="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
            </div>
            <div>
              <p class="text-sm text-gray-600">总回答数</p>
              <p class="text-2xl font-bold text-gray-800">{{ responses.length }}</p>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-lg p-6 shadow-sm">
          <div class="flex items-center gap-4">
            <div class="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div>
              <p class="text-sm text-gray-600">完成率</p>
              <p class="text-2xl font-bold text-gray-800">{{ completionRate }}%</p>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-lg p-6 shadow-sm">
          <div class="flex items-center gap-4">
            <div class="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-purple-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div>
              <p class="text-sm text-gray-600">平均完成时间</p>
              <p class="text-2xl font-bold text-gray-800">2-3分钟</p>
            </div>
          </div>
        </div>
      </div>

      <div class="space-y-6">
        <div v-for="(question, index) in survey?.questions" :key="question.id" class="bg-white rounded-lg shadow-sm p-6">
          <div class="flex items-center gap-3 mb-4">
            <span class="w-8 h-8 bg-blue-500 text-white rounded-full flex items-center justify-center text-sm font-medium">{{ index + 1 }}</span>
            <div class="flex-1">
              <h3 class="font-semibold text-gray-800">{{ question.title }}</h3>
              <p class="text-sm text-gray-600">{{ getQuestionTypeName(question.type) }} · {{ getAnswerCount(question.id) }} 人回答</p>
            </div>
          </div>

          <div v-if="['single', 'multiple', 'dropdown'].includes(question.type)" class="space-y-3">
            <div v-for="option in question.options" :key="option.id" class="flex items-center gap-4">
              <span class="w-32 text-sm text-gray-600 truncate">{{ option.text }}</span>
              <div class="flex-1">
                <div class="h-8 bg-gray-100 rounded-full overflow-hidden">
                  <div class="h-full bg-blue-500 rounded-full flex items-center justify-end px-3 transition-all duration-500" :style="{ width: `${getPercentage(question.id, option.id)}%`, minWidth: getPercentage(question.id, option.id) > 0 ? '40px' : '0' }">
                    <span v-if="getPercentage(question.id, option.id) > 10" class="text-white text-sm font-medium">{{ getPercentage(question.id, option.id) }}%</span>
                  </div>
                </div>
              </div>
              <span class="w-16 text-sm text-gray-800 text-right">{{ getOptionCount(question.id, option.id) }}人</span>
            </div>
          </div>

          <div v-else class="space-y-2">
            <div class="p-4 bg-gray-50 rounded-lg text-sm text-gray-600">共 {{ getTextAnswers(question.id).length }} 条回答</div>
            <div v-if="getTextAnswers(question.id).length > 0" class="max-h-60 overflow-y-auto space-y-2">
              <div v-for="(answer, idx) in getTextAnswers(question.id).slice(0, 5)" :key="idx" class="p-3 bg-gray-50 rounded-lg text-sm">{{ answer || '(空)' }}</div>
              <p v-if="getTextAnswers(question.id).length > 5" class="text-sm text-gray-600 text-center">还有 {{ getTextAnswers(question.id).length - 5 }} 条回答...</p>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useSurveyStore } from '../stores/survey'

const router = useRouter()
const route = useRoute()
const surveyStore = useSurveyStore()
const survey = ref(null)
const responses = ref([])

const getQuestionTypeName = (type) => ({ single: '单选题', multiple: '多选题', dropdown: '下拉框', text: '文本框' })[type] || type

const completionRate = computed(() => {
  if (!survey.value || responses.value.length === 0) return 0
  const completedResponses = responses.value.filter(r => r.answers.length === survey.value.questions.length).length
  return Math.round((completedResponses / responses.value.length) * 100)
})

const getAnswerCount = (questionId) => responses.value.filter(r => r.answers.find(a => a.questionId === questionId)).length

const getOptionCount = (questionId, optionId) => {
  return responses.value.filter(r => {
    const answer = r.answers.find(a => a.questionId === questionId)
    if (!answer) return false
    if (Array.isArray(answer.value)) return answer.value.includes(optionId)
    return answer.value === optionId
  }).length
}

const getPercentage = (questionId, optionId) => {
  const total = getAnswerCount(questionId)
  if (total === 0) return 0
  return Math.round((getOptionCount(questionId, optionId) / total) * 100)
}

const getTextAnswers = (questionId) => responses.value.map(r => r.answers.find(a => a.questionId === questionId)?.value).filter(v => v)

const goBack = () => { router.push('/admin') }

const exportData = () => {
  const data = { survey: survey.value, responses: responses.value, exportTime: new Date().toISOString() }
  const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `${survey.value?.title || 'survey'}_responses.json`
  a.click()
  URL.revokeObjectURL(url)
}

onMounted(async () => {
  await surveyStore.loadSurveys()
  survey.value = surveyStore.getSurveyById(route.params.id)
  responses.value = surveyStore.getSurveyResponses(route.params.id)
})
</script>
