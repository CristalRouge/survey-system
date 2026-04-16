<template>
  <div class="space-y-3">
    <label
      v-for="option in options"
      :key="option.id"
      class="flex items-center p-4 border rounded-lg cursor-pointer transition-colors"
      :class="isSelected(option.id) ? 'border-blue-500 bg-blue-50' : 'border-gray-200 hover:border-blue-300'"
    >
      <input
        type="checkbox"
        :value="option.id"
        :checked="isSelected(option.id)"
        @change="toggleOption(option.id)"
        class="w-5 h-5 text-blue-500 rounded"
      />
      <span class="ml-3 text-gray-700">{{ option.text }}</span>
    </label>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  modelValue: { type: Array, default: () => [] },
  options: { type: Array, default: () => [] }
})

const emit = defineEmits(['update:modelValue'])

const isSelected = (optionId) => props.modelValue.includes(optionId)

const toggleOption = (optionId) => {
  const newValue = [...props.modelValue]
  const index = newValue.indexOf(optionId)
  if (index > -1) {
    newValue.splice(index, 1)
  } else {
    newValue.push(optionId)
  }
  emit('update:modelValue', newValue)
}
</script>
