<template>
  <div>
    <section class="paper-card page-intro">
      <div>
        <p class="eyebrow">READER SPACE</p>
        <h1>我的阅读空间</h1>
        <p>这里是读者端首页，只保留读者需要的找书、预约、借阅记录、阅读目标和个性推荐，不展示后台管理统计。</p>
      </div>
      <el-button class="primary-action" @click="$router.push('/books')">去探索图书</el-button>
    </section>

    <div class="dashboard-grid">
      <section class="paper-card metric-card">
        <span>图书探索</span>
        <strong>找书</strong>
        <p>按书名、作者、分类和库存筛选馆藏图书。</p>
      </section>
      <section class="paper-card metric-card">
        <span>我的借阅</span>
        <strong>记录</strong>
        <p>查看自己当前借阅、已归还和逾期记录。</p>
      </section>
      <section class="paper-card metric-card">
        <span>阅读目标</span>
        <strong>成长</strong>
        <p>设置本月或本学期阅读目标，归还后自动累计完成数。</p>
      </section>
      <section class="paper-card metric-card">
        <span>猜你喜欢</span>
        <strong>推荐</strong>
        <p>根据历史借阅分类推荐适合继续阅读的图书。</p>
      </section>
    </div>
  </div>
</template>
