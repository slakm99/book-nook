<template>
  <section class="paper-card page-section">
    <div class="toolbar-line">
      <div>
        <p class="eyebrow">READER</p>
        <h1>读者管理</h1>
        <p class="muted-text">有借阅记录的读者不建议物理删除，可通过停用保留历史数据。</p>
      </div>
      <el-button class="primary-action" @click="open()">新增读者</el-button>
    </div>

    <el-form inline>
      <el-form-item label="搜索">
        <el-input v-model="query.keyword" clearable placeholder="姓名 / 学号 / 电话" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" clearable style="width: 120px">
          <el-option label="正常" value="1" />
          <el-option label="停用" value="0" />
        </el-select>
      </el-form-item>
      <el-button @click="search">搜索</el-button>
    </el-form>

    <el-table :data="rows" v-loading="loading" border empty-text="暂无读者数据">
      <el-table-column prop="student_no" label="学号" min-width="120" />
      <el-table-column prop="name" label="姓名" min-width="100" />
      <el-table-column prop="phone" label="电话" min-width="130" />
      <el-table-column prop="college" label="学院" min-width="130" />
      <el-table-column prop="grade" label="年级" width="100" />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="Number(row.status) === 1 ? 'success' : 'info'">
            {{ Number(row.status) === 1 ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="230">
        <template #default="{ row }">
          <el-button text @click="open(row)">编辑</el-button>
          <el-button
            text
            :type="Number(row.status) === 1 ? 'warning' : 'success'"
            @click="toggleStatus(row)"
          >
            {{ Number(row.status) === 1 ? '停用' : '启用' }}
          </el-button>
          <el-button text type="danger" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap">
      <el-pagination
        v-model:current-page="query.page"
        v-model:page-size="query.size"
        :page-sizes="[5, 10, 20, 30]"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @size-change="load"
        @current-change="load"
      />
    </div>

    <el-dialog v-model="visible" title="读者信息" width="520px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="学号" prop="studentNo">
          <el-input v-model="form.studentNo" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="学院">
          <el-input v-model="form.college" />
        </el-form-item>
        <el-form-item label="年级">
          <el-input v-model="form.grade" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio-button :label="1">正常</el-radio-button>
            <el-radio-button :label="0">停用</el-radio-button>
          </el-radio-group>
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
import { nextTick, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { api } from '../api/library'

const rows = ref([])
const total = ref(0)
const loading = ref(false)
const visible = ref(false)
const formRef = ref()

const query = reactive({ page: 1, size: 5, keyword: '', status: '' })
const form = reactive({
  id: null,
  studentNo: '',
  name: '',
  phone: '',
  college: '',
  grade: '',
  status: 1
})

const rules = {
  studentNo: [
    { required: true, message: '请输入学号', trigger: 'blur' },
    { min: 6, max: 30, message: '学号长度在 6-30 个字符之间', trigger: 'blur' }
  ],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }]
}

async function load() {
  loading.value = true
  try {
    const r = await api.readers(query)
    rows.value = r.data.list
    total.value = r.data.total
  } finally {
    loading.value = false
  }
}

function search() {
  query.page = 1
  load()
}

function open(row) {
  Object.assign(
    form,
    row
      ? {
          id: row.id,
          studentNo: row.student_no,
          name: row.name,
          phone: row.phone,
          college: row.college,
          grade: row.grade,
          status: Number(row.status)
        }
      : { id: null, studentNo: '', name: '', phone: '', college: '', grade: '', status: 1 }
  )
  visible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

async function save() {
  await formRef.value?.validate()
  await api.saveReader(form)
  ElMessage.success('保存成功')
  visible.value = false
  load()
}

async function toggleStatus(row) {
  const nextStatus = Number(row.status) === 1 ? 0 : 1
  const action = nextStatus === 1 ? '启用' : '停用'
  await ElMessageBox.confirm(`确认${action}读者「${row.name}」吗？`)
  await api.saveReader({
    id: row.id,
    studentNo: row.student_no,
    name: row.name,
    phone: row.phone,
    college: row.college,
    grade: row.grade,
    status: nextStatus
  })
  ElMessage.success(`${action}成功`)
  load()
}

async function remove(id) {
  await ElMessageBox.confirm('确认删除读者？如果读者已有借阅记录，系统会阻止删除，建议改为停用。')
  await api.deleteReader(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>
