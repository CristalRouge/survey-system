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
            <h1 class="text-xl font-bold text-gray-800">{{ isEdit ? '编辑问卷' : '创建问卷' }}</h1>
          </div>
          <div class="flex items-center gap-3">
            <el-button @click="saveDraft">保存草稿</el-button>
            <el-button type="primary" @click="publishSurvey">{{ isPublished ? '更新发布' : '发布问卷' }}</el-button>
          </div>
        </div>
      </div>
    </header>

    <main class="max-w-7xl mx-auto px-4 py-6">
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div class="lg:col-span-1 space-y-6">
          <div class="bg-white rounded-lg shadow-sm p-6">
            <h3 class="font-semibold text-gray-800 mb-4">问卷设置</h3>
            <el-form label-position="top">
              <el-form-item label="问卷标题">
                <el-input v-model="survey.title" placeholder="请输入问卷标题" />
              </el-form-item>
              <el-form-item label="问卷描述">
                <el-input v-model="survey.description" type="textarea" :rows="3" placeholder="请输入问卷描述" />
              </el-form-item>
              <el-form-item label="封面图片URL">
                <el-input v-model="survey.coverImage" placeholder="请输入图片URL" />
                <div v-if="survey.coverImage" class="mt-2">
                  <img :src="survey.coverImage" alt="封面预览" class="w-full h-32 object-cover rounded-lg" />
                </div>
              </el-form-item>
              <el-form-item label="问卷状态">
                <el-tag :type="survey.status === 'published' ? 'success' : 'warning'">
                  {{ survey.status === 'published' ? '已发布' : '草稿' }}
                </el-tag>
              </el-form-item>
            </el-form>
          </div>
        </div>

        <div class="lg:col-span-2">
          <div class="bg-white rounded-lg shadow-sm">
            <div class="p-4 border-b border-gray-200 flex items-center justify-between">
              <h3 class="font-semibold text-gray-800">问题列表 ({{ survey.questions.length }})</h3>
              <el-dropdown trigger="click" @command="addQuestion">
                <el-button type="primary" size="small">
                  添加问题
                  <svg class="w-4 h-4 ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                  </svg>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="single">单选题</el-dropdown-item>
                    <el-dropdown-item command="multiple">多选题</el-dropdown-item>
                    <el-dropdown-item command="dropdown">下拉框</el-dropdown-item>
                    <el-dropdown-item command="text">文本框</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>

            <div class="p-4 space-y-4">
              <div v-if="survey.questions.length === 0" class="text-center py-12 text-gray-600">
                <svg class="w-12 h-12 mx-auto mb-4 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                </svg>
                <p>暂无问题，点击上方按钮添加</p>
              </div>

              <div v-for="(question, index) in survey.questions" :key="question.id" class="border border-gray-200 rounded-lg p-4">
                <div class="flex items-start justify-between mb-4">
                  <div class="flex items-center gap-2">
                    <span class="w-8 h-8 bg-blue-500 text-white rounded-full flex items-center justify-center text-sm font-medium">{{ index + 1 }}</span>
                    <span class="px-2 py-1 bg-gray-100 text-gray-600 text-xs rounded">{{ getQuestionTypeName(question.type) }}</span>
                    <span v-if="question.required" class="px-2 py-1 bg-red-100 text-red-500 text-xs rounded">必填</span>
                  </div>
                  <div class="flex items-center gap-2">
                    <button @click="moveQuestion(index, -1)" :disabled="index === 0" class="p-1 hover:bg-gray-100 rounded disabled:opacity-50">
                      <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 15l7-7 7 7" /></svg>
                    </button>
                    <button @click="moveQuestion(index, 1)" :disabled="index === survey.questions.length - 1" class="p-1 hover:bg-gray-100 rounded disabled:opacity-50">
                      <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
                    </button>
                    <el-popconfirm title="确定删除此问题？" @confirm="deleteQuestion(index)">
                      <template #reference>
                        <button class="p-1 hover:bg-red-100 text-red-500 rounded">
                          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
                        </button>
                      </template>
                    </el-popconfirm>
                  </div>
                </div>

                <el-form label-position="top" class="space-y-4">
                  <el-form-item label="问题标题" class="mb-2">
                    <el-input v-model="question.title" placeholder="请输入问题标题" />
                  </el-form-item>
                  <el-form-item label="问题描述（可选）">
                    <el-input v-model="question.description" placeholder="请输入问题描述" />
                  </el-form-item>
                  <el-form-item label="问题图片URL（可选）">
                    <el-input v-model="question.image" placeholder="请输入图片URL" />
                    <div v-if="question.image" class="mt-2">
                      <img :src="question.image" alt="问题图片预览" class="w-full max-h-40 object-contain rounded-lg" />
                    </div>
                  </el-form-item>
                  <el-form-item>
                    <el-checkbox v-model="question.required">设为必填</el-checkbox>
                  </el-form-item>

                  <div v-if="['single', 'multiple', 'dropdown'].includes(question.type)" class="space-y-2">
                    <label class="text-sm font-medium text-gray-800">选项</label>
                    <div v-for="(option, optIndex) in question.options" :key="option.id" class="flex items-center gap-2">
                      <el-input v-model="option.text" placeholder="选项内容" class="flex-1" />
                      <button @click="removeOption(question, optIndex)" class="p-2 hover:bg-red-100 text-red-500 rounded">
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
                      </button>
                    </div>
                    <el-button size="small" @click="addOption(question)" class="mt-2">
                      <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" /></svg>
                      添加选项
                    </el-button>
                  </div>

                  <div v-if="question.type === 'text'" class="space-y-4">
                    <el-form-item label="占位提示文字">
                      <el-input v-model="question.placeholder" placeholder="请输入占位文字" />
                    </el-form-item>
                  </div>
                </el-form>
              </div>
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
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const surveyStore = useSurveyStore()

const isEdit = computed(() => route.params.id && route.params.id !== 'create')

const survey = ref({
  id: '', title: '', description: '', coverImage: '', status: 'draft', questions: []
})

const isPublished = computed(() => survey.value.status === 'published')

const getQuestionTypeName = (type) => {
  const names = { single: '单选题', multiple: '多选题', dropdown: '下拉框', text: '文本框' }
  return names[type] || type
}

const generateId = () => `q-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`
const generateOptId = () => `opt-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`

const addQuestion = (type) => {
  const question = {
    id: generateId(), type, title: '', description: '', image: '', required: false, placeholder: '',
    options: ['single', 'multiple', 'dropdown'].includes(type) ? [
      { id: generateOptId(), text: '选项1', image: '' },
      { id: generateOptId(), text: '选项2', image: '' }
    ] : []
  }
  survey.value.questions.push(question)
}

const deleteQuestion = (index) => { survey.value.questions.splice(index, 1) }

const moveQuestion = (index, direction) => {
  const newIndex = index + direction
  if (newIndex < 0 || newIndex >= survey.value.questions.length) return
  const questions = [...survey.value.questions]
  ;[questions[index], questions[newIndex]] = [questions[newIndex], questions[index]]
  survey.value.questions = questions
}

const addOption = (question) => { question.options.push({ id: generateOptId(), text: '', image: '' }) }

const removeOption = (question, index) => {
  if (question.options.length <= 2) { ElMessage.warning('至少需要两个选项'); return }
  question.options.splice(index, 1)
}

const goBack = () => { router.push('/admin') }

const saveDraft = async () => {
  if (!survey.value.title.trim()) { ElMessage.warning('请输入问卷标题'); return }
  survey.value.status = 'draft'
  await saveSurvey()
  ElMessage.success('草稿已保存')
}

const publishSurvey = async () => {
  if (!survey.value.title.trim()) { ElMessage.warning('请输入问卷标题'); return }
  if (survey.value.questions.length === 0) { ElMessage.warning('请至少添加一个问题'); return }
  survey.value.status = 'published'
  await saveSurvey()
  ElMessage.success('问卷已发布')
  router.push('/admin')
}

const saveSurvey = async () => {
  if (isEdit.value) { await surveyStore.updateSurvey(survey.value.id, survey.value) }
  else {
    const newSurvey = await surveyStore.createSurvey(survey.value)
    survey.value.id = newSurvey.id
    router.replace(`/admin/survey/${newSurvey.id}`)
  }
}

onMounted(async () => {
  await surveyStore.loadSurveys()
  if (isEdit.value) {
    const existingSurvey = surveyStore.getSurveyById(route.params.id)
    if (existingSurvey) {
      survey.value = JSON.parse(JSON.stringify(existingSurvey))
    }
  }
})
</script>
