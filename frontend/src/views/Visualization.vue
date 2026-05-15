<template>
  <div class="visualization-container">
    <el-header class="app-header">
      <div class="header-left">
        <h1 class="app-title">图谱可视化</h1>
      </div>
      <div class="header-right">
        <span class="user-info">{{ userStore.username }}</span>
        <el-button type="danger" @click="handleLogout">退出登录</el-button>
      </div>
    </el-header>

    <el-container class="main-container">
      <el-aside width="200px" class="app-sidebar">
        <el-menu :default-active="activeMenu" router>
          <el-menu-item index="/">仪表盘</el-menu-item>
          <el-menu-item index="/compounds">化合物</el-menu-item>
          <el-menu-item index="/reaction-paths">反应路径</el-menu-item>
          <el-menu-item index="/visualization">可视化</el-menu-item>
        </el-menu>
      </el-aside>

      <el-main class="app-main">
        <div class="visualization-wrapper">
          <div class="controls-panel">
            <el-card>
              <template #header>
                <div class="card-header">
                  <span>算法工具</span>
                </div>
              </template>

              <el-form label-width="100px">
                <el-form-item label="起始节点">
                  <el-input-number v-model="startNodeId" :min="1"></el-input-number>
                </el-form-item>
                <el-form-item label="终点节点">
                  <el-input-number v-model="endNodeId" :min="1"></el-input-number>
                </el-form-item>
              </el-form>

              <div class="button-group">
                <el-button type="primary" @click="findShortestPath">
                  BFS 最短路径
                </el-button>
                <el-button type="success" @click="findLowestEnergyPath">
                  Dijkstra 最低能量
                </el-button>
                <el-button type="info" @click="resetVisualization">
                  重置
                </el-button>
              </div>
              <div v-if="pathResult" class="path-result">
                <p><strong>路径：</strong> {{ pathResult.path.join(' → ') }}</p>
                <p><strong>路径长度：</strong> {{ pathResult.length }} 个节点</p>
              </div>
            </el-card>
          </div>

          <div class="chart-container">
            <div ref="chartRef" class="chart"></div>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { compoundAPI, reactionPathAPI } from '../api/index'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const router = useRouter()
const userStore = useUserStore()

const activeMenu = ref('/visualization')
const chartRef = ref(null)
let chart = null

const startNodeId = ref(null)
const endNodeId = ref(null)
const pathResult = ref(null)

const compounds = ref([])
const paths = ref([])

onMounted(async () => {
  await fetchData()
  await nextTick()
  initChart()
})

const fetchData = async () => {
  try {
    const [compoundsResponse, pathsResponse] = await Promise.all([
      compoundAPI.getAllCompounds(),
      reactionPathAPI.getAllReactionPaths()
    ])

    if (compoundsResponse.code === 200) {
      compounds.value = compoundsResponse.data
    }
    if (pathsResponse.code === 200) {
      paths.value = pathsResponse.data
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  }
}

const initChart = () => {
  if (chartRef.value) {
    chart = echarts.init(chartRef.value)
    updateChart()
    window.addEventListener('resize', () => {
      chart.resize()
    })
  }
}

const updateChart = () => {
  if (!chart) return

  // Build nodes
  const nodes = compounds.value.map(compound => ({
    id: String(compound.id),
    name: compound.name,
    symbolSize: 40,
    itemStyle: { color: '#409eff' }
  }))

  // Build links
  const links = paths.value.map(path => ({
    source: String(path.fromCompoundId),
    target: String(path.toCompoundId),
    label: { content: path.reactionType || '' }
  }))

  const option = {
    title: {
      text: '反应路径网络图',
      left: 'center',
      textStyle: {
        fontSize: 18,
        fontWeight: 600
      }
    },
    tooltip: {
      formatter: function(params) {
        if (params.dataType === 'node') {
          return `${params.data.name} (ID: ${params.data.id})`
        } else if (params.dataType === 'edge') {
          return `${params.data.source} → ${params.data.target}`
        }
      }
    },
    series: [{
      type: 'graph',
      layout: 'force',
      draggable: true,
      nodes: nodes,
      links: links,
      roam: true,
      edgeSymbol: ['none', 'arrow'],
      edgeSymbolSize: 8,
      label: {
        show: true,
        position: 'right',
        formatter: '{b}',
        color: '#333'
      },
      lineStyle: {
        color: '#999',
        curveness: 0.2
      },
      emphasis: {
        focus: 'adjacency',
        lineStyle: {
          width: 2
        }
      },
      force: {
        repulsion: 200,
        friction: 0.7,
        gravity: 0.15,
        edgeLength: [100, 200]
      }
    }]
  }

  chart.setOption(option)
}

const findShortestPath = async () => {
  if (!startNodeId.value || !endNodeId.value) {
    ElMessage.warning('请先选择起始节点和终点节点')
    return
  }

  try {
    const response = await reactionPathAPI.findShortestPath(
      startNodeId.value,
      endNodeId.value
    )
    if (response.code === 200) {
      pathResult.value = response.data
      highlightPath(response.data.path)
    } else {
      ElMessage.error('未找到路径')
    }
  } catch (error) {
    ElMessage.error('查找最短路径失败')
  }
}

const findLowestEnergyPath = async () => {
  if (!startNodeId.value || !endNodeId.value) {
    ElMessage.warning('请先选择起始节点和终点节点')
    return
  }

  try {
    const response = await reactionPathAPI.findLowestEnergyPath(
      startNodeId.value,
      endNodeId.value
    )
    if (response.code === 200) {
      pathResult.value = response.data
      highlightPath(response.data.path)
    } else {
      ElMessage.error('未找到路径')
    }
  } catch (error) {
    ElMessage.error('查找最低能量路径失败')
  }
}

const highlightPath = (pathNodes) => {
  if (!chart) return

  const normalizedPath = pathNodes.map(String)

  const nodes = compounds.value.map(compound => {
    const id = String(compound.id)
    const inPath = normalizedPath.includes(id)
    return {
      id,
      name: compound.name,
      symbolSize: inPath ? 50 : 40,
      itemStyle: {
        color: inPath ? '#f56c6c' : '#409eff'
      }
    }
  })

  const links = paths.value.map(path => {
    const source = String(path.fromCompoundId)
    const target = String(path.toCompoundId)
    const active = isPathEdge(source, target, normalizedPath)
    return {
      source,
      target,
      label: { content: path.reactionType || '' },
      lineStyle: {
        width: active ? 3 : 1,
        color: active ? '#f56c6c' : '#999'
      }
    }
  })

  const option = chart.getOption()
  option.series[0].nodes = nodes
  option.series[0].links = links
  chart.setOption(option)
}

const isPathEdge = (source, target, pathNodes) => {
  for (let i = 0; i < pathNodes.length - 1; i++) {
    if (pathNodes[i] === source && pathNodes[i + 1] === target) {
      return true
    }
  }
  return false
}

const resetVisualization = () => {
  pathResult.value = null
  startNodeId.value = null
  endNodeId.value = null
  updateChart()
}

const handleLogout = () => {
  userStore.logout()
  ElMessage.success('退出登录成功')
  router.push('/login')
}
</script>

<style scoped>
.visualization-container {
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
}

.app-title {
  margin: 0;
  font-size: 20px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.main-container {
  flex: 1;
  overflow: hidden;
}

.app-sidebar {
  background-color: #f5f5f5;
  border-right: 1px solid #ddd;
}

.app-main {
  overflow: hidden;
  display: flex;
}

.visualization-wrapper {
  display: flex;
  align-items: stretch;
  height: 100%;
  gap: 20px;
  padding: 20px;
  width: 100%;
}

.controls-panel {
  width: 320px;
  min-width: 320px;
  overflow-y: auto;
  align-self: flex-start;
}

.chart-container {
  flex: 1;
  min-height: 600px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.chart {
  width: 100%;
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.button-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 20px;
}

.button-group .el-button {
  width: 100% !important;
  justify-content: center;
}

.path-result {
  background-color: #f0f9ff;
  border: 1px solid #b3d8ff;
  border-radius: 4px;
  padding: 12px;
  margin-top: 20px;
  font-size: 12px;
}

.path-result p {
  margin: 8px 0;
}
</style>
