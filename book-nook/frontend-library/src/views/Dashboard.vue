<template>
  <div>
    <section class="paper-card hero-card">
      <p class="eyebrow">READING SPACE</p>
      <h1>今晚适合挑一本书，坐在窗边慢慢读。</h1>
      <p>这里汇总校园图书馆的馆藏、借阅、预约与阅读成长数据，帮助管理员和馆员快速掌握图书流通情况。</p>
    </section>

    <div class="dashboard-grid">
      <section v-for="item in metrics" :key="item.label" class="paper-card metric-card">
        <span>{{ item.label }}</span>
        <strong>{{ item.value }}</strong>
        <p>{{ item.tip }}</p>
      </section>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { api } from '../api/library'

const overview = ref({})

const metrics = computed(() => [
  { label: '馆藏图书', value: overview.value.book_total || 0, tip: '当前系统收录书目' },
  { label: '可借库存', value: overview.value.available_total || 0, tip: '当前可借册数' },
  { label: '注册读者', value: overview.value.reader_total || 0, tip: '校园读者数量' },
  { label: '待处理预约', value: overview.value.reservation_total || 0, tip: '等待或可取书预约' }
])

onMounted(async () => {
  overview.value = (await api.overview()).data || {}
})
</script>
