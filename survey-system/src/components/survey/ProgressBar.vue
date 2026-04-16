<template>
  <div class="w-full bg-gray-200 rounded-full h-2.5">
    <div
      class="bg-blue-500 h-2.5 rounded-full transition-all duration-300"
      :style="{ width: `${progress}%` }"
    ></div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  current: { type: Number, required: true },
  total: { type: Number, required: true }
})

const progress = computed(() => {
  if (props.total === 0) return 0
  return Math.round((props.current / props.total) * 100)
})
</script>
