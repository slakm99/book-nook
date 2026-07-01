<template>
  <section>
    <div class="dashboard-grid">
      <section v-for="item in cards" :key="item.label" class="paper-card metric-card">
        <span>{{ item.label }}</span>
        <strong>{{ item.value }}</strong>
        <p>{{ item.tip }}</p>
      </section>
    </div>

    <div class="chart-grid">
      <div ref="trendRef" class="paper-card chart-card"></div>
      <div ref="pieRef" class="paper-card chart-card"></div>
      <div ref="hotRef" class="paper-card chart-card"></div>
    </div>
  </section>
</template>

<script setup>
import { computed, nextTick, onMounted, onUnmounted, ref } from 'vue'
import * as echarts from 'echarts'
import { api } from '../api/library'

const overview = ref({})
const trend = ref([])
const distribution = ref([])
const trendRef = ref()
const pieRef = ref()
const hotRef = ref()
const charts = []

const cards = computed(() => [
  { label: '图书总量', value: overview.value.book_total || 0, tip: '馆藏书目' },
  { label: '可借库存', value: overview.value.available_total || 0, tip: '当前可借册数' },
  { label: '读者数量', value: overview.value.reader_total || 0, tip: '注册读者' },
  { label: '逾期记录', value: overview.value.overdue_total || 0, tip: '需要重点处理' }
])

function createChart(el) {
  const chart = echarts.init(el)
  charts.push(chart)
  return chart
}

function draw() {
  charts.splice(0).forEach(chart => chart.dispose())

  createChart(trendRef.value).setOption({
    title: { text: '借阅趋势' },
    xAxis: { type: 'category', data: trend.value.map(item => item.name) },
    yAxis: { type: 'value' },
    series: [{ type: 'line', smooth: true, data: trend.value.map(item => item.value), areaStyle: {} }]
  })

  createChart(pieRef.value).setOption({
    title: { text: '分类借阅占比' },
    tooltip: {},
    series: [{ type: 'pie', radius: ['45%', '70%'], data: distribution.value }]
  })

  const hotBooks = overview.value.hotBooks || []
  createChart(hotRef.value).setOption({
    title: { text: '热门图书' },
    xAxis: { type: 'value' },
    yAxis: { type: 'category', data: hotBooks.map(item => item.name) },
    series: [{ type: 'bar', data: hotBooks.map(item => item.value) }]
  })
}

onMounted(async () => {
  overview.value = (await api.overview()).data || {}
  trend.value = (await api.trend()).data || []
  distribution.value = (await api.categoryDistribution()).data || []
  await nextTick()
  draw()
})

onUnmounted(() => {
  charts.splice(0).forEach(chart => chart.dispose())
})
</script>
