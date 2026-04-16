<template>
  <div class="space-y-3">
    <label
      v-for="option in options"
      :key="option.id"
      class="flex items-center p-4 border rounded-lg cursor-pointer transition-colors"
      :class="modelValue === option.id ? 'border-blue-500 bg-blue-50' : 'border-gray-200 hover:border-blue-300'"
    >
      <input
        type="radio"
        :name="name"
        :value="option.id"
        :checked="modelValue === option.id"
        @change="$emit('update:modelValue', option.id)"
        class="w-5 h-5 text-blue-500"
      />
      <span class="ml-3 text-gray-700">{{ option.text }}</span>
    </label>
  </div>
</template>

<script setup>
defineProps({
  modelValue: { type: [String, Number], default: null },
  options: { type: Array, default: () => [] },
  name: { type: String, required: true }
})
defineEmits(['update:modelValue'])
</script>
