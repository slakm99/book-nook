<template>
  <section class="paper-card page-section">
    <div class="toolbar-line">
      <div>
        <p class="eyebrow">RESERVATION</p>
        <h1>{{ $route.meta.title }}</h1>
        <p class="muted-text">
          预约不会自动变成借阅。图书可取后，馆员点击“办理取书”，系统才生成借阅记录并扣减库存。
        </p>
      </div>
      <el-button v-if="isManager" class="primary-action" @click="openDialog">新增预约</el-button>
    </div>

    <el-form inline>
      <el-form-item label="搜索">
        <el-input v-model="query.keyword" clearable placeholder="读者 / 图书" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" clearable style="width: 150px">
          <el-option label="等待排队" value="WAITING" />
          <el-option label="可取书" value="READY" />
          <el-option label="已完成" value="FINISHED" />
          <el-option label="已取消" value="CANCELLED" />
          <el-option label="已过期" value="EXPIRED" />
        </el-select>
      </el-form-item>
      <el-button @click="search">搜索</el-button>
    </el-form>

    <el-table :data="rows" v-loading="loading" border empty-text="暂无预约记录">
      <el-table-column prop="reader_name" label="读者" />
      <el-table-column prop="book_title" label="图书" />
      <el-table-column prop="queue_no" label="排队号" width="90" />
      <el-table-column label="状态" width="110">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="pickup_deadline" label="取书截止" min-width="160" />
      <el-table-column label="操作" width="190">
        <template #default="{ row }">
          <el-button v-if="isManager && row.status === 'READY'" text @click="pickup(row.id)">
            办理取书
          </el-button>
          <el-button v-if="['WAITING', 'READY'].includes(row.status)" text type="danger" @click="cancel(row.id)">
            取消
          </el-button>
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

    <el-dialog v-model="visible" title="新增预约" width="560px">
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
            placeholder="请选择预约图书"
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
        <el-button class="primary-action" :loading="submitLoading" @click="reserve">确认预约</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
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

const query = reactive({ page: 1, size: 5, keyword: '', status: '' })
const form = reactive({ readerId: '', bookId: '' })

const currentUser = computed(() => getUser())
const isManager = computed(() => ['ADMIN', 'LIBRARIAN'].includes(currentUser.value.role))

async function load() {
  loading.value = true
  try {
    const r = await api.reservations(query)
    rows.value = r.data.list
    total.value = r.data.total
  } finally {
    loading.value = false
  }
}

async function loadOptions() {
  if (!isManager.value) return
  const [readerRes, bookRes] = await Promise.all([
    api.readers({ page: 1, size: 100, status: 1 }),
    getBookList({ page: 1, size: 100 })
  ])
  readerOptions.value = readerRes.data.list || []
  bookOptions.value = bookRes.data.list || []
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

async function reserve() {
  if (!isManager.value) return
  if (!form.readerId || !form.bookId) {
    ElMessage.warning('请先选择读者和图书')
    return
  }
  submitLoading.value = true
  try {
    await api.reserve(form)
    ElMessage.success('预约创建成功')
    visible.value = false
    load()
  } finally {
    submitLoading.value = false
  }
}

async function cancel(id) {
  await ElMessageBox.confirm('确认取消这条预约吗？')
  await api.cancelReservation(id)
  ElMessage.success('预约已取消')
  load()
}

async function pickup(id) {
  await ElMessageBox.confirm('确认读者已到馆取书，并办理借出吗？')
  await api.pickupReservation(id)
  ElMessage.success('已生成借阅记录')
  load()
}

function statusText(status) {
  return {
    WAITING: '等待排队',
    READY: '可取书',
    FINISHED: '已完成',
    CANCELLED: '已取消',
    EXPIRED: '已过期'
  }[status] || status
}

function statusType(status) {
  return {
    READY: 'success',
    WAITING: 'warning',
    FINISHED: 'info',
    CANCELLED: 'danger',
    EXPIRED: 'danger'
  }[status] || ''
}

onMounted(load)
</script>
