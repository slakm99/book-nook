<template>
  <section class="paper-card page-section">
    <div class="toolbar-line"><div><p class="eyebrow">BORROW</p><h1>{{ $route.meta.title }}</h1></div><el-button v-if="isManager" class="primary-action" @click="visible=true">办理借书</el-button></div>
    <el-form inline><el-form-item label="搜索"><el-input v-model="query.keyword" clearable placeholder="读者/图书" /></el-form-item><el-form-item label="状态"><el-select v-model="query.status" clearable style="width:130px"><el-option label="借阅中" value="BORROWED"/><el-option label="逾期" value="OVERDUE"/><el-option label="已归还" value="RETURNED"/></el-select></el-form-item><el-button @click="search">搜索</el-button></el-form>
    <el-table :data="rows" v-loading="loading" border>
      <el-table-column prop="reader_name" label="读者" /><el-table-column prop="book_title" label="图书" /><el-table-column prop="borrow_time" label="借出时间" min-width="160" /><el-table-column prop="due_time" label="应还时间" min-width="160" /><el-table-column prop="display_status" label="状态" width="100" />
      <el-table-column v-if="isManager" label="操作" width="120"><template #default="{ row }"><el-button v-if="row.status !== 'RETURNED'" text @click="returnIt(row.id)">归还</el-button></template></el-table-column>
    </el-table>
    <div class="pagination-wrap"><el-pagination v-model:current-page="query.page" :total="total" layout="total, prev, pager, next" @current-change="load"/></div>
    <el-dialog v-model="visible" title="办理借书" width="420px"><el-form :model="form" label-width="90px"><el-form-item label="读者ID"><el-input v-model="form.readerId"/></el-form-item><el-form-item label="图书ID"><el-input v-model="form.bookId"/></el-form-item></el-form><template #footer><el-button @click="visible=false">取消</el-button><el-button class="primary-action" @click="borrow">确认借出</el-button></template></el-dialog>
  </section>
</template>
<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { api } from '../api/library'
import { getUser } from '../utils/auth'
const rows=ref([]),total=ref(0),loading=ref(false),visible=ref(false)
const query=reactive({page:1,size:10,keyword:'',status:''}); const form=reactive({readerId:'1',bookId:'1'})
const currentUser = computed(() => getUser())
const isManager = computed(() => ['ADMIN', 'LIBRARIAN'].includes(currentUser.value.role))
async function load(){loading.value=true;try{const r=await api.borrows(query);rows.value=r.data.list;total.value=r.data.total}finally{loading.value=false}}
function search(){query.page=1;load()}
async function borrow(){if(!isManager.value)return;await api.borrow(form);ElMessage.success('借书成功');visible.value=false;load()}
async function returnIt(id){if(!isManager.value)return;await api.returnBook(id);ElMessage.success('归还成功');load()}
onMounted(load)
</script>
