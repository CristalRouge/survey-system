<template>
  <div id="app" class="min-h-screen bg-gray-50">
    <router-view />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useSurveyStore } from './stores/survey'

const surveyStore = useSurveyStore()

onMounted(() => {
  surveyStore.loadSurveys()
})
</script>
