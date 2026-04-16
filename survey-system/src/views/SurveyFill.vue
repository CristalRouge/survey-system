<template>
  <div class="min-h-screen bg-gray-50">
    <header class="bg-white shadow-sm sticky top-0 z-50">
      <div class="max-w-2xl mx-auto px-4 py-4">
        <router-link to="/" class="flex items-center gap-2 text-gray-600 hover:text-blue-500 transition-colors">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
          </svg>
          返回
        </router-link>
      </div>
    </header>

    <div v-if="loading" class="flex justify-center py-20">
      <div class="w-10 h-10 border-4 border-blue-500 border-t-transparent rounded-full animate-spin"></div>
    </div>

    <div v-else-if="!survey" class="text-center py-20">
      <p class="text-gray-600">问卷不存在</p>
      <router-link to="/" class="text-blue-500 hover:underline mt-2 inline-block">返回首页</router-link>
    </div>

    <main v-else class="max-w-2xl mx-auto px-4 py-6 pb-24">
      <div class="text-center mb-8">
        <h1 class="text-2xl md:text-3xl font-bold text-gray-800 mb-3">{{ survey.title }}</h1>
        <p class="text-gray-600">{{ survey.description }}</p>
      </div>

      <div v-if="survey.coverImage" class="mb-8 rounded-lg overflow-hidden">
        <img :src="survey.coverImage" :alt="survey.title" class="w-full h-auto" />
      </div>

      <div class="mb-8">
        <div class="flex justify-between text-sm text-gray-600 mb-2">
          <span>完成进度</span>
          <span>{{ progressPercent }}%</span>
        </div>
        <div class="w-full bg-gray-200 rounded-full h-2">
          <div class="h-full bg-blue-500 rounded-full transition-all duration-300" :style="{ width: progressPercent + '%' }"></div>
        </div>
      </div>

      <div class="space-y-6">
        <div v-for="(question, index) in survey.questions" :key="question.id" class="bg-white rounded-xl p-6 shadow-sm">
          <div class="mb-4">
            <h3 class="text-lg font-semibold text-gray-800">
              {{ index + 1 }}. {{ question.title }}
              <span v-if="question.required" class="text-red-500 ml-1">*</span>
            </h3>
            <p v-if="question.description" class="text-sm text-gray-600 mt-1">{{ question.description }}</p>
          </div>

          <div v-if="question.image" class="mb-4">
            <img :src="question.image" alt="问题配图" class="max-w-full h-auto rounded-lg" />
          </div>

          <div v-if="question.type === 'single'" class="space-y-3">
            <label v-for="option in question.options" :key="option.id" class="flex items-center gap-3 p-4 rounded-lg border-2 cursor-pointer transition-all" :class="answers[question.id] === option.id ? 'border-blue-500 bg-blue-50' : 'border-gray-200 hover:border-blue-300'">
              <input type="radio" :name="question.id" :value="option.id" :checked="answers[question.id] === option.id" class="w-5 h-5 text-blue-600" @change="answers[question.id] = option.id" />
              <span class="text-gray-800">{{ option.text }}</span>
            </label>
          </div>

          <div v-else-if="question.type === 'multiple'" class="space-y-3">
            <label v-for="option in question.options" :key="option.id" class="flex items-center gap-3 p-4 rounded-lg border-2 cursor-pointer transition-all" :class="isMultipleSelected(question.id, option.id) ? 'border-blue-500 bg-blue-50' : 'border-gray-200 hover:border-blue-300'">
              <input type="checkbox" :value="option.id" :checked="isMultipleSelected(question.id, option.id)" class="w-5 h-5 text-blue-600 rounded" @change="toggleMultiple(question.id, option.id)" />
              <span class="text-gray-800">{{ option.text }}</span>
            </label>
          </div>

          <div v-else-if="question.type === 'dropdown'" class="relative">
            <select v-model="answers[question.id]" class="w-full px-4 py-3 border-2 border-gray-200 rounded-lg focus:border-blue-500 focus:outline-none appearance-none bg-white">
              <option value="">请选择...</option>
              <option v-for="option in question.options" :key="option.id" :value="option.id">{{ option.text }}</option>
            </select>
            <svg class="w-5 h-5 absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
            </svg>
          </div>

          <div v-else-if="question.type === 'text'">
            <textarea v-model="answers[question.id]" :placeholder="question.placeholder || '请输入...'" rows="4" class="w-full px-4 py-3 border-2 border-gray-200 rounded-lg focus:border-blue-500 focus:outline-none resize-none"></textarea>
          </div>
        </div>
      </div>

      <div v-if="validationErrors.length > 0" class="mt-6 p-4 bg-red-50 border border-red-200 rounded-lg">
        <p class="font-medium text-red-600">请完成以下必填项：</p>
        <ul class="mt-1 text-sm text-red-600">
          <li v-for="error in validationErrors" :key="error">{{ error }}</li>
        </ul>
      </div>
    </main>

    <div class="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 p-4">
      <div class="max-w-2xl mx-auto">
        <button @click="submitSurvey" :disabled="isSubmitting" class="w-full py-4 text-lg font-semibold text-white rounded-lg transition-colors" :class="isSubmitting ? 'bg-gray-400 cursor-not-allowed' : 'bg-blue-500 hover:bg-blue-600'">
          <span v-if="isSubmitting" class="flex items-center justify-center gap-2">
            <div class="w-5 h-5 border-3 border-white border-t-transparent rounded-full animate-spin"></div>
            提交中...
          </span>
          <span v-else>提交问卷</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useSurveyStore } from '../stores/survey'

const route = useRoute()
const router = useRouter()
const surveyStore = useSurveyStore()
const survey = ref(null)
const loading = ref(true)
const answers = ref({})
const isSubmitting = ref(false)

onMounted(async () => {
  await surveyStore.loadSurveys()
  survey.value = surveyStore.getSurveyById(route.params.id)
  loading.value = false
  const saved = localStorage.getItem(`survey-progress-${route.params.id}`)
  if (saved) {
    try { answers.value = JSON.parse(saved) } catch (e) { console.error('恢复进度失败:', e) }
  }
})

watch(answers, (newAnswers) => {
  if (survey.value) localStorage.setItem(`survey-progress-${survey.value.id}`, JSON.stringify(newAnswers))
}, { deep: true })

const progressPercent = computed(() => {
  if (!survey.value) return 0
  const total = survey.value.questions.length
  if (total === 0) return 0
  const answered = survey.value.questions.filter(q => {
    const answer = answers.value[q.id]
    if (Array.isArray(answer)) return answer.length > 0
    return !!answer
  }).length
  return Math.round((answered / total) * 100)
})

const validationErrors = computed(() => {
  if (!survey.value) return []
  const errors = []
  survey.value.questions.forEach((q, index) => {
    if (q.required) {
      const answer = answers.value[q.id]
      if (Array.isArray(answer)) {
        if (answer.length === 0) errors.push(`第${index + 1}题：${q.title}`)
      } else if (!answer || (typeof answer === 'string' && !answer.trim())) {
        errors.push(`第${index + 1}题：${q.title}`)
      }
    }
  })
  return errors
})

const isMultipleSelected = (questionId, optionId) => {
  return Array.isArray(answers.value[questionId]) && answers.value[questionId].includes(optionId)
}

const toggleMultiple = (questionId, optionId) => {
  if (!answers.value[questionId]) answers.value[questionId] = []
  const index = answers.value[questionId].indexOf(optionId)
  if (index > -1) answers.value[questionId].splice(index, 1)
  else answers.value[questionId].push(optionId)
}

const submitSurvey = async () => {
  if (validationErrors.value.length > 0) return
  isSubmitting.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    const formattedAnswers = Object.entries(answers.value).map(([questionId, value]) => ({ questionId, value }))
    surveyStore.submitResponse(survey.value.id, formattedAnswers)
    localStorage.removeItem(`survey-progress-${survey.value.id}`)
    router.push({ path: '/survey/success', query: { title: survey.value.title } })
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    isSubmitting.value = false
  }
}
</script>
