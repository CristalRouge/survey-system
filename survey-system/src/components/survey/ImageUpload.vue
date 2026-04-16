<template>
  <div class="image-upload">
    <div v-if="!modelValue" class="upload-placeholder" @click="triggerUpload">
      <svg class="w-12 h-12 text-gray-400 mx-auto mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
      </svg>
      <p class="text-gray-500 text-sm">点击上传图片</p>
      <p class="text-gray-400 text-xs mt-1">支持 JPG、PNG，最大5MB</p>
    </div>

    <div v-else class="upload-preview">
      <img :src="modelValue" alt="预览" class="preview-image" />
      <button v-if="!disabled" class="remove-btn" @click="removeImage">
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>
    </div>

    <input
      ref="fileInput"
      type="file"
      accept="image/*"
      class="hidden"
      @change="handleFileChange"
    />

    <!-- 上传中状态 -->
    <div v-if="uploading" class="upload-loading">
      <div class="spinner"></div>
      <span class="text-sm text-gray-500">上传中...</span>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { fileApi } from '../../api/index.js'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  disabled: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue'])

const fileInput = ref(null)
const uploading = ref(false)

const triggerUpload = () => {
  if (!props.disabled) {
    fileInput.value.click()
  }
}

const handleFileChange = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  // 验证文件类型
  if (!file.type.startsWith('image/')) {
    alert('请选择图片文件')
    return
  }

  // 验证文件大小 (5MB)
  if (file.size > 5 * 1024 * 1024) {
    alert('图片大小不能超过5MB')
    return
  }

  uploading.value = true

  try {
    const res = await fileApi.upload(file)
    if (res.code === 200) {
      emit('update:modelValue', res.data)
    } else {
      alert(res.message || '上传失败')
    }
  } catch (err) {
    console.error('Upload failed:', err)
    alert('上传失败: ' + err.message)
  } finally {
    uploading.value = false
    // 清空input
    event.target.value = ''
  }
}

const removeImage = () => {
  emit('update:modelValue', '')
}
</script>

<style scoped>
.image-upload {
  @apply relative;
}

.upload-placeholder {
  @apply border-2 border-dashed border-gray-300 rounded-lg p-6 text-center cursor-pointer transition-colors;
}

.upload-placeholder:hover {
  @apply border-blue-400 bg-blue-50;
}

.upload-preview {
  @apply relative rounded-lg overflow-hidden;
}

.preview-image {
  @apply w-full h-40 object-cover rounded-lg;
}

.remove-btn {
  @apply absolute top-2 right-2 w-8 h-8 bg-red-500 text-white rounded-full flex items-center justify-center hover:bg-red-600 transition-colors;
}

.upload-loading {
  @apply absolute inset-0 bg-white/80 flex flex-col items-center justify-center rounded-lg;
}

.spinner {
  @apply w-8 h-8 border-4 border-blue-500 border-t-transparent rounded-full animate-spin mb-2;
}
</style>
