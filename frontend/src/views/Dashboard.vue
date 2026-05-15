<template>
  <div class="dashboard-container">
    <el-header class="app-header">
      <div class="header-left">
        <h1 class="app-title">仪表盘</h1>
      </div>
      <div class="header-right">
        <span class="user-info">{{ userStore.username }}</span>
        <el-button type="danger" @click="handleLogout">退出登录</el-button>
      </div>
    </el-header>

    <el-container class="main-container">
      <el-aside width="200px" class="app-sidebar">
        <el-menu :default-active="activeMenu" router>
          <el-menu-item index="/">
            <i class="el-icon-home"></i>
            <span>仪表盘</span>
          </el-menu-item>
          <el-menu-item index="/compounds">
            <i class="el-icon-document"></i>
            <span>化合物</span>
          </el-menu-item>
          <el-menu-item index="/reaction-paths">
            <i class="el-icon-connection"></i>
            <span>反应路径</span>
          </el-menu-item>
          <el-menu-item index="/visualization">
            <i class="el-icon-picture"></i>
            <span>可视化</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-main class="app-main">
        <div class="dashboard-content">
          <h2>欢迎，{{ userStore.username }}！</h2>
          <p>这是一个基于图论的反应路径网络可视化系统。</p>

          <el-row :gutter="20" class="stats-row">
            <el-col :xs="24" :sm="12" :md="6">
              <div class="stat-card">
                <div class="stat-value">{{ stats.compounds }}</div>
                <div class="stat-label">化合物数量</div>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="6">
              <div class="stat-card">
                <div class="stat-value">{{ stats.paths }}</div>
                <div class="stat-label">反应路径数量</div>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="6">
              <div class="stat-card">
                <div class="stat-value">{{ stats.users }}</div>
                <div class="stat-label">用户数</div>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="6">
              <div class="stat-card">
                <div class="stat-value">{{ stats.algorithms }}</div>
                <div class="stat-label">算法数量</div>
              </div>
            </el-col>
          </el-row>

          <div class="features-section">
            <h3>功能</h3>
            <el-row :gutter="20">
              <el-col :xs="24" :sm="12" :md="8">
                <div class="feature-card">
                  <h4>化合物管理</h4>
                  <p>新增、编辑、删除和搜索化合物信息。</p>
                </div>
              </el-col>
              <el-col :xs="24" :sm="12" :md="8">
                <div class="feature-card">
                  <h4>反应路径</h4>
                  <p>管理化学反应路径及其关联关系。</p>
                </div>
              </el-col>
              <el-col :xs="24" :sm="12" :md="8">
                <div class="feature-card">
                  <h4>图谱可视化</h4>
                  <p>支持节点拖拽与缩放的交互式可视化。</p>
                </div>
              </el-col>
            </el-row>

            <el-row :gutter="20" class="margin-top">
              <el-col :xs="24" :sm="12" :md="8">
                <div class="feature-card">
                  <h4>BFS 算法</h4>
                  <p>查找化合物之间的最短路径。</p>
                </div>
              </el-col>
              <el-col :xs="24" :sm="12" :md="8">
                <div class="feature-card">
                  <h4>Dijkstra 算法</h4>
                  <p>查找最低能量的反应路径。</p>
                </div>
              </el-col>
              <el-col :xs="24" :sm="12" :md="8">
                <div class="feature-card">
                  <h4>用户认证</h4>
                  <p>基于 JWT 的安全登录。</p>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const activeMenu = ref('/')
const stats = ref({
  compounds: 0,
  paths: 0,
  users: 1,
  algorithms: 2
})

onMounted(() => {
  // TODO: Fetch real statistics from backend
})

const handleLogout = () => {
  userStore.logout()
  ElMessage.success('退出登录成功')
  router.push('/login')
}
</script>

<style scoped>
.dashboard-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-header {
  background-color: #409eff;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  align-items: center;
}

.app-title {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  font-size: 14px;
}

.main-container {
  flex: 1;
  overflow: hidden;
}

.app-sidebar {
  background-color: #f5f5f5;
  border-right: 1px solid #ddd;
  overflow-y: auto;
}

.app-main {
  flex: 1;
  overflow-y: auto;
  background-color: #f9f9f9;
}

.dashboard-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.dashboard-content h2 {
  color: #333;
  margin-bottom: 10px;
}

.dashboard-content > p {
  color: #666;
  margin-bottom: 30px;
}

.stats-row {
  margin: 30px 0;
}

.stat-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.features-section {
  margin-top: 30px;
}

.features-section h3 {
  color: #333;
  margin-bottom: 20px;
  font-size: 20px;
}

.feature-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
}

.feature-card:hover {
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.feature-card h4 {
  color: #333;
  margin: 0 0 10px 0;
  font-size: 16px;
}

.feature-card p {
  color: #666;
  margin: 0;
  font-size: 14px;
  line-height: 1.5;
}

.margin-top {
  margin-top: 20px;
}
</style>
