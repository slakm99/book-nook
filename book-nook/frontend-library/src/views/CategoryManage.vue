<template>
  <section class="paper-card page-section">
    <div class="toolbar-line">
      <div><p class="eyebrow">CATEGORY</p><h1>图书分类</h1></div>
      <el-button class="primary-action" @click="open()">新增分类</el-button>
    </div>
    <el-table :data="rows" v-loading="loading" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="category_name" label="分类名称" />
      <el-table-column prop="description" label="说明" />
      <el-table-column prop="sort_order" label="排序" width="90" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }"><el-button text @click="open(row)">编辑</el-button><el-button text type="danger" @click="remove(row.id)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="visible" title="分类信息" width="460px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="名称" prop="categoryName"><el-input v-model="form.categoryName" /></el-form-item>
        <el-form-item label="说明"><el-input v-model="form.description" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="visible=false">取消</el-button><el-button class="primary-action" @click="save">保存</el-button></template>
    </el-dialog>
  </section>
</template>
<script setup>
import { nextTick, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { api } from '../api/library'
const rows = ref([]), loading = ref(false), visible = ref(false)
const formRef = ref()
const form = reactive({ id: null, categoryName: '', description: '', sortOrder: 0, status: 1 })
const rules = {
  categoryName: [
    { required: true, message: '请输入分类名称', trigger: 'blur' },
    { min: 2, max: 30, message: '分类名称长度在 2-30 个字符之间', trigger: 'blur' }
  ]
}
async function load(){ loading.value=true; try{ rows.value=(await api.categories()).data||[] } finally{ loading.value=false } }
function open(row){ Object.assign(form, row ? { id: row.id, categoryName: row.category_name, description: row.description, sortOrder: row.sort_order, status: row.status } : { id:null, categoryName:'', description:'', sortOrder:0, status:1 }); visible.value=true; nextTick(() => formRef.value?.clearValidate()) }
async function save(){ await formRef.value?.validate(); await api.saveCategory(form); ElMessage.success('保存成功'); visible.value=false; load() }
async function remove(id){ await ElMessageBox.confirm('确认删除该分类？'); await api.deleteCategory(id); ElMessage.success('删除成功'); load() }
onMounted(load)
</script>
