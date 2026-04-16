<template>
  <div class="min-h-screen bg-gray-50">
    <header class="bg-white shadow-sm sticky top-0 z-50">
      <div class="max-w-7xl mx-auto px-4 py-4">
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-3">
            <router-link to="/" class="flex items-center gap-3">
              <div class="w-10 h-10 bg-blue-500 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
                </svg>
              </div>
              <h1 class="text-xl font-bold text-gray-800">问卷管理系统</h1>
            </router-link>
          </div>
          <div class="flex items-center gap-4">
            <span class="text-sm text-gray-600">欢迎使用</span>
            <router-link to="/" class="px-4 py-2 text-sm font-medium text-blue-600 hover:bg-blue-50 rounded-lg transition-colors">
              返回首页
            </router-link>
          </div>
        </div>
      </div>
    </header>

    <main class="max-w-7xl mx-auto px-4 py-8">
      <div class="flex items-center justify-between mb-8">
        <div>
          <h2 class="text-2xl font-bold text-gray-800">问卷管理</h2>
          <p class="text-gray-600 mt-1">管理所有问卷，查看统计数据</p>
        </div>
        <el-button type="primary" @click="createSurvey">
          <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
          </svg>
          创建问卷
        </el-button>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <div class="bg-white rounded-lg p-6 shadow-sm">
          <div class="flex items-center gap-4">
            <div class="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
              </svg>
            </div>
            <div>
              <p class="text-sm text-gray-600">问卷总数</p>
              <p class="text-2xl font-bold text-gray-800">{{ surveys.length }}</p>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-lg p-6 shadow-sm">
          <div class="flex items-center gap-4">
            <div class="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div>
              <p class="text-sm text-gray-600">已发布</p>
              <p class="text-2xl font-bold text-gray-800">{{ publishedCount }}</p>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-lg p-6 shadow-sm">
          <div class="flex items-center gap-4">
            <div class="w-12 h-12 bg-yellow-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-yellow-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div>
              <p class="text-sm text-gray-600">草稿</p>
              <p class="text-2xl font-bold text-gray-800">{{ draftCount }}</p>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-lg p-6 shadow-sm">
          <div class="flex items-center gap-4">
            <div class="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-purple-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
            </div>
            <div>
              <p class="text-sm text-gray-600">总回答数</p>
              <p class="text-2xl font-bold text-gray-800">{{ totalResponses }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow-sm">
        <div class="p-4 border-b border-gray-200">
          <div class="flex items-center justify-between">
            <h3 class="font-semibold text-gray-800">问卷列表</h3>
            <el-input v-model="searchKeyword" placeholder="搜索问卷..." class="w-64" clearable>
              <template #prefix>
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
              </template>
            </el-input>
          </div>
        </div>

        <el-table :data="filteredSurveys" style="width: 100%" v-loading="loading">
          <el-table-column prop="title" label="问卷标题" min-width="200">
            <template #default="{ row }">
              <div>
                <div class="font-medium text-gray-800">{{ row.title }}</div>
                <div class="text-sm text-gray-600 truncate">{{ row.description }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="120">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="questions" label="问题数" width="100" align="center">
            <template #default="{ row }">
              <span class="text-gray-800">{{ row.questions?.length || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="responseCount" label="回答数" width="100" align="center">
            <template #default="{ row }">
              <span class="text-gray-800">{{ row.responseCount || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="updatedAt" label="更新时间" width="180">
            <template #default="{ row }">
              {{ formatDate(row.updatedAt) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="280" fixed="right">
            <template #default="{ row }">
              <div class="flex items-center gap-2">
                <el-button size="small" @click="editSurvey(row.id)">编辑</el-button>
                <el-button size="small" @click="previewSurvey(row.id)">预览</el-button>
                <el-dropdown trigger="click">
                  <el-button size="small">
                    更多
                    <svg class="w-4 h-4 ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                    </svg>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item @click="toggleStatus(row)">
                        {{ row.status === 'published' ? '关闭' : '发布' }}
                      </el-dropdown-item>
                      <el-dropdown-item @click="viewResponses(row.id)">查看回答</el-dropdown-item>
                      <el-dropdown-item divided @click="deleteSurveyConfirm(row)">删除</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useSurveyStore } from '../stores/survey'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const surveyStore = useSurveyStore()
const searchKeyword = ref('')

const loading = computed(() => surveyStore.loading)
const surveys = computed(() => surveyStore.surveys)
const publishedCount = computed(() => surveys.value.filter(s => s.status === 'published').length)
const draftCount = computed(() => surveys.value.filter(s => s.status === 'draft').length)
const totalResponses = computed(() => surveys.value.reduce((sum, s) => sum + (s.responseCount || 0), 0))

const filteredSurveys = computed(() => {
  if (!searchKeyword.value) return surveys.value
  return surveys.value.filter(s => s.title.toLowerCase().includes(searchKeyword.value.toLowerCase()) || s.description.toLowerCase().includes(searchKeyword.value.toLowerCase()))
})

const getStatusType = (status) => {
  const types = { published: 'success', draft: 'warning', closed: 'info' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { published: '已发布', draft: '草稿', closed: '已关闭' }
  return texts[status] || status
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })
}

const createSurvey = () => { router.push('/admin/survey/create') }
const editSurvey = (id) => { router.push(`/admin/survey/${id}`) }
const previewSurvey = (id) => { window.open(`/survey/${id}`, '_blank') }

const toggleStatus = async (survey) => {
  const newStatus = survey.status === 'published' ? 'closed' : 'published'
  await surveyStore.updateSurveyStatus(survey.id, newStatus)
  ElMessage.success(newStatus === 'published' ? '问卷已发布' : '问卷已关闭')
}

const viewResponses = (id) => { router.push(`/admin/responses/${id}`) }

const deleteSurveyConfirm = (survey) => {
  ElMessageBox.confirm(`确定要删除问卷"${survey.title}"吗？此操作不可恢复。`, '删除确认', { confirmButtonText: '删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      await surveyStore.deleteSurvey(survey.id)
      ElMessage.success('问卷已删除')
    }).catch(() => {})
}

onMounted(async () => {
  await surveyStore.loadSurveys()
})
</script>
