<template>
  <section class="paper-card page-section">
    <div class="toolbar-line">
      <div>
        <p class="eyebrow">GOAL</p>
        <h1>阅读目标</h1>
      </div>
      <el-button class="primary-action" @click="visible = true">设置目标</el-button>
    </div>

    <el-table :data="rows" border>
      <el-table-column prop="target_period" label="周期" />
      <el-table-column prop="target_count" label="目标" />
      <el-table-column prop="completed_count" label="已完成" />
      <el-table-column label="进度">
        <template #default="{ row }">
          <el-progress :percentage="progress(row)" />
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="visible" title="设置阅读目标" width="420px">
      <el-form :model="form" label-width="90px">
        <el-form-item v-if="isManager" label="读者ID">
          <el-input v-model="form.readerId" placeholder="管理员/馆员代设置时填写" />
        </el-form-item>
        <el-form-item label="周期">
          <el-input v-model="form.targetPeriod" placeholder="如 2026-07" />
        </el-form-item>
        <el-form-item label="目标数量">
          <el-input-number v-model="form.targetCount" :min="1" />
        </el-form-item>
        <el-form-item label="已完成">
          <el-input-number v-model="form.completedCount" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button class="primary-action" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { api } from '../api/library'
import { getUser } from '../utils/auth'

const rows = ref([])
const visible = ref(false)
const user = computed(() => getUser())
const isManager = computed(() => ['ADMIN', 'LIBRARIAN'].includes(user.value.role))
const form = reactive({
  readerId: '',
  targetPeriod: new Date().toISOString().slice(0, 7),
  targetCount: 4,
  completedCount: 0
})

function currentReaderId() {
  return user.value.role === 'READER' ? user.value.refId : form.readerId
}

function progress(row) {
  return Math.min(100, Math.round((row.completed_count / row.target_count) * 100 || 0))
}

async function load() {
  rows.value = (await api.goals({ readerId: currentReaderId() })).data || []
}

async function save() {
  await api.saveGoal({ ...form, readerId: currentReaderId() })
  ElMessage.success('目标已保存')
  visible.value = false
  load()
}

onMounted(() => {
  if (user.value.role === 'READER') form.readerId = user.value.refId
  load()
})
</script>
