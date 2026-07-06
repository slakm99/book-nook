<template>
  <section class="page-section">
    <div class="toolbar-line">
      <div>
        <p class="eyebrow">MY READING DATA</p>
        <h1>我的统计</h1>
        <p class="muted-text">这里只展示你自己的借阅情况，不展示全馆管理统计。</p>
      </div>
      <el-button class="primary-action" :loading="loading" @click="loadData">刷新数据</el-button>
    </div>

    <div class="dashboard-grid">
      <div v-for="item in cards" :key="item.label" class="paper-card metric-card">
        <span>{{ item.label }}</span>
        <strong>{{ item.value }}</strong>
        <p>{{ item.tip }}</p>
      </div>
    </div>

    <div class="chart-grid">
      <div ref="trendRef" class="paper-card chart-card"></div>
      <div ref="categoryRef" class="paper-card chart-card"></div>
    </div>

    <el-empty v-if="!loading && emptyData" description="暂无借阅统计数据，去图书探索页借一本书吧。" />
  </section>
</template>

<script setup>
import { computed, nextTick, onMounted, onUnmounted, ref } from 'vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'
import { api } from '../api/library'

const loading = ref(false)
const overview = ref({})
const trend = ref([])
const categoryDistribution = ref([])
const trendRef = ref()
const categoryRef = ref()
const charts = []

const cards = computed(() => [
  { label: '借阅总数', value: overview.value.borrow_total || 0, tip: '累计借过的图书' },
  { label: '当前在借', value: overview.value.active_total || 0, tip: '尚未归还的图书' },
  { label: '已归还', value: overview.value.returned_total || 0, tip: '已经完成归还' },
  { label: '逾期数量', value: overview.value.overdue_total || 0, tip: '需要尽快处理' },
  { label: '有效预约', value: overview.value.reservation_total || 0, tip: '等待或可取书预约' }
])

const emptyData = computed(() =>
  Number(overview.value.borrow_total || 0) === 0 &&
  Number(overview.value.reservation_total || 0) === 0
)

function createChart(el) {
  const chart = echarts.init(el)
  charts.push(chart)
  return chart
}

function disposeCharts() {
  charts.splice(0).forEach(chart => chart.dispose())
}

function drawCharts() {
  disposeCharts()
  if (!trendRef.value || !categoryRef.value) return

  createChart(trendRef.value).setOption({
    title: { text: '近 30 天借阅趋势', left: 'center' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: trend.value.map(item => item.name) },
    yAxis: { type: 'value', minInterval: 1 },
    grid: { left: 40, right: 20, bottom: 40, top: 70 },
    series: [
      {
        name: '借阅本数',
        type: 'line',
        smooth: true,
        areaStyle: {},
        data: trend.value.map(item => item.value)
      }
    ]
  })

  createChart(categoryRef.value).setOption({
    title: { text: '我的借阅分类占比', left: 'center' },
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [
      {
        name: '借阅分类',
        type: 'pie',
        radius: ['42%', '68%'],
        center: ['50%', '48%'],
        data: categoryDistribution.value
      }
    ]
  })
}

async function loadData() {
  loading.value = true
  try {
    const res = await api.readerStats()
    overview.value = res.data || {}
    trend.value = res.data?.trend || []
    categoryDistribution.value = res.data?.categoryDistribution || []
    await nextTick()
    drawCharts()
  } catch (error) {
    ElMessage.error('加载个人统计失败')
  } finally {
    loading.value = false
  }
}

function resizeCharts() {
  charts.forEach(chart => chart.resize())
}

onMounted(() => {
  loadData()
  window.addEventListener('resize', resizeCharts)
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeCharts)
  disposeCharts()
})
</script>
