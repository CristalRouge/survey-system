<template>
  <div class="min-h-screen bg-gray-50 flex items-center justify-center px-4">
    <div class="max-w-md w-full text-center">
      <div class="mb-8">
        <div class="w-24 h-24 mx-auto bg-green-100 rounded-full flex items-center justify-center animate-bounce">
          <svg class="w-12 h-12 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
          </svg>
        </div>
      </div>
      <h1 class="text-2xl md:text-3xl font-bold text-gray-800 mb-4">提交成功!</h1>
      <p class="text-gray-600 mb-2">{{ surveyTitle ? `感谢您填写"${surveyTitle}"` : '感谢您的参与' }}</p>
      <p class="text-gray-600 mb-8">您的反馈对我们非常重要，我们将根据您的意见不断改进。</p>
      <router-link to="/" class="block w-full py-4 text-lg font-semibold text-white bg-blue-500 hover:bg-blue-600 rounded-lg transition-colors">
        返回首页
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
const route = useRoute()
const surveyTitle = computed(() => route.query.title || '')
</script>
