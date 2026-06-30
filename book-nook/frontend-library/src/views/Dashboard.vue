<template>
  <div class="dashboard-grid">
    <section class="paper-card hero-card">
      <p class="eyebrow">READING SPACE</p>
      <h1>今晚适合挑一本书，坐在窗边慢慢读。</h1>
      <p>这里汇总校园图书馆的馆藏、借阅、预约与阅读成长数据，帮助管理员快速掌握图书流通情况。</p>
    </section>
    <section class="paper-card metric-card" v-for="item in metrics" :key="item.label">
      <span>{{ item.label }}</span>
      <strong>{{ item.value }}</strong>
      <p>{{ item.tip }}</p>
    </section>
  </div>
</template>

<script setup>
const metrics = [
  { label: '馆藏图书', value: '20+', tip: '覆盖经典文学、技术与通识读物' },
  { label: '图书分类', value: '6', tip: '覆盖文学、技术、心理等' },
  { label: '系统角色', value: '3', tip: '管理员 / 馆员 / 读者' }
]
</script>
