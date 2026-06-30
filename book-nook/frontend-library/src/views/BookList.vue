<template>
  <div>
    <section class="paper-card page-intro">
      <div>
        <p class="eyebrow">BOOK EXPLORATION</p>
        <h1>图书探索</h1>
        <p>按关键词、作者、分类和库存状态筛选馆藏。无封面图时会生成纸张质感渐变封面。</p>
      </div>
      <div class="toolbar-actions">
        <el-button v-if="isManager" class="primary-action" @click="openBook()">新增图书</el-button>
        <el-segmented v-model="viewMode" :options="['卡片', '表格']" />
      </div>
    </section>

    <section class="paper-card filter-card">
      <el-form :inline="true" :model="query">
        <el-form-item label="关键词">
          <el-input v-model="query.keyword" clearable placeholder="书名 / ISBN / 标签" />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="query.author" clearable placeholder="作者" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="query.categoryId" clearable placeholder="全部分类" style="width: 160px">
            <el-option v-for="item in categories" :key="item.id" :label="item.category_name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="库存">
          <el-select v-model="query.stockStatus" clearable placeholder="全部" style="width: 130px">
            <el-option label="有可借库存" value="available" />
            <el-option label="暂无库存" value="empty" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button class="primary-action" @click="search">搜索</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </section>

    <el-skeleton :loading="loading" animated :rows="6">
      <el-empty v-if="!books.length" description="没有找到符合条件的图书" />

      <div v-else-if="viewMode === '卡片'" class="book-grid">
        <article v-for="book in books" :key="book.id" class="book-card">
          <div class="book-cover" :style="coverStyle(book)">
            <span>{{ book.title?.slice(0, 8) }}</span>
          </div>
          <div class="book-info">
            <el-tag size="small" class="category-tag">{{ book.category_name }}</el-tag>
            <h3>{{ book.title }}</h3>
            <p>{{ book.author }} · {{ book.publisher }}</p>
            <p class="book-description" :title="book.description">{{ book.description || '暂无简介，等待馆员补充这本书的阅读亮点。' }}</p>
            <div class="book-tags">
              <span v-for="tag in splitTags(book.tags)" :key="tag">{{ tag }}</span>
            </div>
            <div class="stock-line">
              <strong>可借 {{ book.stock_available }}</strong>
              <span>馆藏 {{ book.stock_total }} · 借阅 {{ book.borrow_count }}</span>
            </div>
            <div class="card-actions">
              <el-button v-if="isReader" size="small" @click="reserveBook(book)">预约</el-button>
              <el-button v-if="isManager" size="small" @click="openBook(book)">编辑</el-button>
              <el-button v-if="isManager" size="small" type="danger" @click="deleteBookRow(book.id)">删除</el-button>
            </div>
          </div>
        </article>
      </div>

      <el-table v-else :data="books" class="paper-table" border>
        <el-table-column prop="isbn" label="ISBN" min-width="150" />
        <el-table-column prop="title" label="书名" min-width="180" />
        <el-table-column prop="author" label="作者" width="120" />
        <el-table-column prop="category_name" label="分类" width="120" />
        <el-table-column label="内容简介" min-width="260">
          <template #default="{ row }">
            <el-tooltip :content="row.description || '暂无简介'" placement="top" :show-after="300">
              <span class="table-description">{{ row.description || '暂无简介' }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="stock_available" label="可借" width="80" />
        <el-table-column prop="borrow_count" label="借阅量" width="90" />
        <el-table-column label="操作" width="190">
          <template #default="{ row }">
            <el-button v-if="isReader" text @click="reserveBook(row)">预约</el-button>
            <el-button v-if="isManager" text @click="openBook(row)">编辑</el-button>
            <el-button v-if="isManager" text type="danger" @click="deleteBookRow(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-skeleton>

    <div class="pagination-wrap">
      <el-pagination
        v-model:current-page="query.page"
        v-model:page-size="query.size"
        :page-sizes="[8, 12, 16, 20]"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @size-change="loadBooks"
        @current-change="loadBooks"
      />
    </div>

    <el-dialog v-model="visible" title="图书信息" width="680px">
      <el-form ref="formRef" :model="form" :rules="bookRules" label-width="100px" class="book-form">
        <el-form-item label="ISBN" prop="isbn"><el-input v-model="form.isbn" /></el-form-item>
        <el-form-item label="书名" prop="title"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="作者" prop="author"><el-input v-model="form.author" /></el-form-item>
        <el-form-item label="出版社"><el-input v-model="form.publisher" /></el-form-item>
        <el-form-item label="分类" prop="categoryId"><el-select v-model="form.categoryId" style="width:100%"><el-option v-for="item in categories" :key="item.id" :label="item.category_name" :value="item.id" /></el-select></el-form-item>
        <el-form-item label="标签"><el-input v-model="form.tags" placeholder="逗号分隔" /></el-form-item>
        <el-form-item label="馆藏总量" prop="stockTotal"><el-input-number v-model="form.stockTotal" :min="0" /></el-form-item>
        <el-form-item label="可借库存" prop="stockAvailable"><el-input-number v-model="form.stockAvailable" :min="0" /></el-form-item>
        <el-form-item label="简介"><el-input v-model="form.description" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="visible=false">取消</el-button><el-button class="primary-action" @click="saveBookRow">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, nextTick, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getBookList, getCategoryList } from '../api/book'
import { api } from '../api/library'
import { getUser } from '../utils/auth'

const loading = ref(false)
const visible = ref(false)
const formRef = ref()
const viewMode = ref('卡片')
const books = ref([])
const categories = ref([])
const total = ref(0)
const currentUser = computed(() => getUser())
const isReader = computed(() => currentUser.value.role === 'READER')
const isManager = computed(() => ['ADMIN', 'LIBRARIAN'].includes(currentUser.value.role))
const query = reactive({ page: 1, size: 8, keyword: '', author: '', categoryId: '', stockStatus: '' })
const form = reactive({ id: null, isbn: '', title: '', author: '', publisher: '', categoryId: '', tags: '', stockTotal: 1, stockAvailable: 1, description: '', coverUrl: '', status: 'ON_SHELF' })
const bookRules = {
  isbn: [
    { required: true, message: '请输入 ISBN', trigger: 'blur' },
    { min: 10, max: 32, message: 'ISBN 长度应在 10-32 位之间', trigger: 'blur' }
  ],
  title: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  stockTotal: [{ required: true, message: '请输入馆藏总量', trigger: 'change' }],
  stockAvailable: [
    { required: true, message: '请输入可借库存', trigger: 'change' },
    {
      validator: (_, value, callback) => {
        if (Number(value) > Number(form.stockTotal)) callback(new Error('可借库存不能大于馆藏总量'))
        else callback()
      },
      trigger: 'change'
    }
  ]
}

async function loadCategories() {
  const res = await getCategoryList()
  categories.value = res.data || []
}

async function loadBooks() {
  loading.value = true
  try {
    const res = await getBookList(query)
    books.value = res.data.list || []
    total.value = res.data.total || 0
  } catch {
    ElMessage.error('图书列表加载失败')
  } finally {
    loading.value = false
  }
}

function search() {
  query.page = 1
  loadBooks()
}

function reset() {
  Object.assign(query, { page: 1, size: 8, keyword: '', author: '', categoryId: '', stockStatus: '' })
  loadBooks()
}

function openBook(row) {
  if (!isManager.value) {
    ElMessage.warning('读者只能浏览和预约图书')
    return
  }
  Object.assign(form, row ? {
    id: row.id, isbn: row.isbn, title: row.title, author: row.author, publisher: row.publisher,
    categoryId: row.category_id, tags: row.tags, stockTotal: row.stock_total,
    stockAvailable: row.stock_available, description: row.description, coverUrl: row.cover_url, status: row.status
  } : { id: null, isbn: '', title: '', author: '', publisher: '', categoryId: categories.value[0]?.id || '', tags: '', stockTotal: 1, stockAvailable: 1, description: '', coverUrl: '', status: 'ON_SHELF' })
  visible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

async function saveBookRow() {
  if (!isManager.value) return
  await formRef.value?.validate()
  await api.saveBook(form)
  ElMessage.success('图书保存成功')
  visible.value = false
  loadBooks()
}

async function deleteBookRow(id) {
  if (!isManager.value) return
  await ElMessageBox.confirm('确认删除该图书？')
  await api.deleteBook(id)
  ElMessage.success('删除成功')
  loadBooks()
}

async function reserveBook(book) {
  const user = getUser()
  const readerId = user.role === 'READER' ? user.refId : '1'
  await api.reserve({ readerId, bookId: book.id })
  ElMessage.success('预约成功')
}

function splitTags(tags = '') {
  return tags.split(',').filter(Boolean).slice(0, 3)
}

function coverStyle(book) {
  const colors = ['#2f5d50,#d9a441', '#6f4f28,#e9d8a6', '#335c67,#f4a261', '#586f6b,#f2cc8f']
  const index = Number(book.id || 0) % colors.length
  const gradient = `linear-gradient(135deg, ${colors[index]})`
  if (book.cover_url) {
    return {
      backgroundImage: `linear-gradient(180deg, rgba(0,0,0,.08), rgba(0,0,0,.42)), url("${book.cover_url}")`,
      backgroundSize: 'cover',
      backgroundPosition: 'center'
    }
  }
  return { background: gradient }
}

onMounted(async () => {
  await loadCategories()
  await loadBooks()
})
</script>
