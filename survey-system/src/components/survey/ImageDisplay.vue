<template>
  <div v-if="src" class="my-4">
    <img :src="src" :alt="alt" class="max-w-full rounded-lg" />
  </div>
</template>

<script setup>
defineProps({
  src: { type: String, default: '' },
  alt: { type: String, default: 'Image' }
})
</script>
