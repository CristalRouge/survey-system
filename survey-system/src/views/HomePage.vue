<template>
  <div class="min-h-screen bg-gray-50">
    <!-- 演示模式提示条 -->
    <div v-if="DEMO_MODE" class="bg-yellow-100 border-b border-yellow-200 py-2 px-4 text-center">
      <span class="text-yellow-800 text-sm">演示模式 - 使用静态数据</span>
      <button @click="toggleMode" class="ml-4 text-xs text-yellow-600 hover:text-yellow-800 underline">切换到API模式</button>
    </div>

    <header class="bg-white shadow-sm sticky top-0 z-50">
      <div class="max-w-6xl mx-auto px-4 py-4">
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-3">
            <div class="w-10 h-10 bg-blue-500 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
              </svg>
            </div>
            <h1 class="text-xl font-bold text-gray-800">问卷调查系统</h1>
          </div>
          <router-link to="/admin" class="px-4 py-2 text-sm font-medium text-blue-600 hover:bg-blue-50 rounded-lg transition-colors">
            管理后台
          </router-link>
        </div>
      </div>
    </header>

    <main class="max-w-6xl mx-auto px-4 py-8">
      <div class="text-center mb-12">
        <h2 class="text-3xl md:text-4xl font-bold text-gray-800 mb-4">参与问卷调查</h2>
        <p class="text-lg text-gray-600">选择您感兴趣的问卷，认真填写并提交</p>
      </div>

      <div v-if="loading" class="flex justify-center py-12">
        <div class="w-10 h-10 border-4 border-blue-500 border-t-transparent rounded-full animate-spin"></div>
      </div>

      <div v-else class="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        <div v-for="survey in publishedSurveys" :key="survey.id" class="bg-white rounded-xl overflow-hidden shadow-sm hover:shadow-md transition-shadow duration-200">
          <div v-if="survey.coverImage" class="aspect-video overflow-hidden">
            <img :src="survey.coverImage" :alt="survey.title" class="w-full h-full object-cover" />
          </div>
          <div v-else class="aspect-video bg-gradient-to-br from-blue-500 to-blue-600 flex items-center justify-center">
            <svg class="w-16 h-16 text-white opacity-50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
          </div>
          <div class="p-5">
            <h3 class="text-lg font-semibold text-gray-800 mb-2 line-clamp-2">{{ survey.title }}</h3>
            <p class="text-sm text-gray-600 mb-4 line-clamp-2">{{ survey.description }}</p>
            <div class="flex items-center justify-between text-sm text-gray-600 mb-4">
              <span class="flex items-center gap-1">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.228 9c.549-1.165 2.03-2 3.772-2 2.21 0 4 1.343 4 3 0 1.4-1.278 2.575-3.006 2.907-.542.104-.994.54-.994 1.093m0 3h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                {{ survey.questions.length }} 题
              </span>
              <span class="flex items-center gap-1">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
                {{ survey.responseCount }} 人已填写
              </span>
            </div>
            <router-link :to="`/survey/${survey.id}`" class="block w-full py-3 text-center font-medium text-white bg-blue-500 hover:bg-blue-600 rounded-lg transition-colors">
              立即填写
            </router-link>
          </div>
        </div>
      </div>

      <div v-if="!loading && publishedSurveys.length === 0" class="text-center py-12">
        <svg class="w-16 h-16 mx-auto text-gray-300 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
        </svg>
        <p class="text-gray-600">暂无问卷</p>
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useSurveyStore } from '../stores/survey'
import { DEMO_MODE } from '../config.js'

const surveyStore = useSurveyStore()
const loading = computed(() => surveyStore.loading)
const publishedSurveys = computed(() => surveyStore.surveys.filter(s => s.status === 'published') || [])

onMounted(() => {
  surveyStore.loadSurveys()
})

const toggleMode = () => {
  if (confirm('要切换模式需要修改源代码中的 DEMO_MODE 配置。请修改 src/config.js 中的 DEMO_MODE 值：\n- true = 演示模式（使用静态JSON）\n- false = API模式（连接后端服务）')) {
    window.open('/src/config.js', '_blank')
  }
}
</script>
