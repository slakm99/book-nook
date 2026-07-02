<template>
  <section class="paper-card page-section">
    <div class="toolbar-line">
      <div>
        <p class="eyebrow">BORROW</p>
        <h1>{{ $route.meta.title }}</h1>
        <p class="muted-text">
          馆员可为读者办理借书；读者端只展示自己的借阅记录，不能办理后台借出操作。
        </p>
      </div>
      <el-button v-if="isManager" class="primary-action" @click="openDialog">办理借书</el-button>
    </div>

    <el-form inline>
      <el-form-item label="搜索">
        <el-input v-model="query.keyword" clearable placeholder="读者 / 图书" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" clearable style="width: 130px">
          <el-option label="借阅中" value="BORROWED" />
          <el-option label="逾期" value="OVERDUE" />
          <el-option label="已归还" value="RETURNED" />
        </el-select>
      </el-form-item>
      <el-button @click="search">搜索</el-button>
    </el-form>

    <el-table :data="rows" v-loading="loading" border empty-text="暂无借阅记录">
      <el-table-column prop="reader_name" label="读者" />
      <el-table-column prop="book_title" label="图书" />
      <el-table-column prop="borrow_time" label="借出时间" min-width="160" />
      <el-table-column prop="due_time" label="应还时间" min-width="160" />
      <el-table-column prop="display_status" label="状态" width="100" />
      <el-table-column v-if="isManager" label="操作" width="120">
        <template #default="{ row }">
          <el-button v-if="row.status !== 'RETURNED'" text @click="returnIt(row.id)">归还</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap">
      <el-pagination
        v-model:current-page="query.page"
        v-model:page-size="query.size"
        :page-sizes="[10, 20, 30]"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @size-change="load"
        @current-change="load"
      />
    </div>

    <el-dialog v-model="visible" title="办理借书" width="560px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="选择读者">
          <el-select
            v-model="form.readerId"
            filterable
            placeholder="请选择读者"
            style="width: 100%"
          >
            <el-option
              v-for="reader in readerOptions"
              :key="reader.id"
              :label="`${reader.name}（${reader.student_no || reader.studentNo}）`"
              :value="reader.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="选择图书">
          <el-select
            v-model="form.bookId"
            filterable
            placeholder="请选择当前可借图书"
            style="width: 100%"
          >
            <el-option
              v-for="book in bookOptions"
              :key="book.id"
              :label="`${book.title}（${book.author}）- 可借${book.stock_available}册`"
              :value="book.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button class="primary-action" :loading="submitLoading" @click="borrow">确认借出</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { api } from '../api/library'
import { getBookList } from '../api/book'
import { getUser } from '../utils/auth'

const rows = ref([])
const total = ref(0)
const loading = ref(false)
const visible = ref(false)
const submitLoading = ref(false)
const readerOptions = ref([])
const bookOptions = ref([])

const query = reactive({ page: 1, size: 10, keyword: '', status: '' })
const form = reactive({ readerId: '', bookId: '' })

const currentUser = computed(() => getUser())
const isManager = computed(() => ['ADMIN', 'LIBRARIAN'].includes(currentUser.value.role))

async function load() {
  loading.value = true
  try {
    const r = await api.borrows(query)
    rows.value = r.data.list
    total.value = r.data.total
  } finally {
    loading.value = false
  }
}

async function loadOptions() {
  if (!isManager.value) return
  const [readerRes, bookRes] = await Promise.all([
    api.readers({ page: 1, size: 100 }),
    getBookList({ page: 1, size: 100, stockStatus: 'available' })
  ])
  readerOptions.value = readerRes.data.list || []
  bookOptions.value = (bookRes.data.list || []).filter(book => Number(book.stock_available) > 0)
  form.readerId = readerOptions.value[0]?.id || ''
  form.bookId = bookOptions.value[0]?.id || ''
}

async function openDialog() {
  visible.value = true
  await loadOptions()
}

function search() {
  query.page = 1
  load()
}

async function borrow() {
  if (!isManager.value) return
  if (!form.readerId || !form.bookId) {
    ElMessage.warning('请先选择读者和图书')
    return
  }
  submitLoading.value = true
  try {
    await api.borrow(form)
    ElMessage.success('借书成功')
    visible.value = false
    load()
  } finally {
    submitLoading.value = false
  }
}

async function returnIt(id) {
  if (!isManager.value) return
  await api.returnBook(id)
  ElMessage.success('归还成功')
  load()
}

onMounted(load)
</script>
