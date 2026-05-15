<template>
  <div class="reaction-paths-container">
    <el-header class="app-header">
      <div class="header-left">
        <h1 class="app-title">反应路径管理</h1>
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
        <div class="content-wrapper">
          <div class="page-header">
            <h2>反应路径</h2>
            <el-button type="primary" @click="showAddDialog = true">
              + 新增反应路径
            </el-button>
          </div>

          <el-table
            :data="paths"
            stripe
            style="width: 100%"
          >
            <el-table-column prop="id" label="ID" width="80"></el-table-column>
            <el-table-column prop="fromCompoundId" label="起始化合物ID"></el-table-column>
            <el-table-column prop="toCompoundId" label="目标化合物ID"></el-table-column>
            <el-table-column prop="reactionType" label="反应类型"></el-table-column>
            <el-table-column prop="energyChange" label="能量变化"></el-table-column>
            <el-table-column prop="activationEnergy" label="活化能"></el-table-column>
            <el-table-column label="操作" width="200">
              <template #default="{ row }">
                <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
                <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- Add/Edit Dialog -->
          <el-dialog
            v-model="showAddDialog"
            :title="editingId ? '编辑反应路径' : '新增反应路径'"
          >
            <el-form :model="form" label-width="140px">
              <el-form-item label="起始化合物ID">
                <el-input-number v-model="form.fromCompoundId"></el-input-number>
              </el-form-item>
              <el-form-item label="目标化合物ID">
                <el-input-number v-model="form.toCompoundId"></el-input-number>
              </el-form-item>
              <el-form-item label="反应类型">
                <el-input v-model="form.reactionType"></el-input>
              </el-form-item>
              <el-form-item label="能量变化">
                <el-input-number v-model="form.energyChange"></el-input-number>
              </el-form-item>
              <el-form-item label="活化能">
                <el-input-number v-model="form.activationEnergy"></el-input-number>
              </el-form-item>
              <el-form-item label="描述">
                <el-input v-model="form.description" type="textarea"></el-input>
              </el-form-item>
            </el-form>
            <template #footer>
              <span class="dialog-footer">
                <el-button @click="showAddDialog = false">取消</el-button>
                <el-button type="primary" @click="handleSave">保存</el-button>
              </span>
            </template>
          </el-dialog>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { reactionPathAPI } from '../api/index'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const activeMenu = ref('/reaction-paths')
const paths = ref([])
const showAddDialog = ref(false)
const editingId = ref(null)

const form = ref({
  fromCompoundId: null,
  toCompoundId: null,
  reactionType: '',
  energyChange: null,
  activationEnergy: null,
  description: ''
})

onMounted(() => {
  fetchPaths()
})

const fetchPaths = async () => {
  try {
    const response = await reactionPathAPI.getAllReactionPaths()
    if (response.code === 200) {
      paths.value = response.data
    }
  } catch (error) {
    ElMessage.error('获取反应路径失败')
  }
}

const handleEdit = (row) => {
  editingId.value = row.id
  form.value = { ...row }
  showAddDialog.value = true
}

const handleSave = async () => {
  try {
    if (editingId.value) {
      const response = await reactionPathAPI.updateReactionPath(editingId.value, form.value)
      if (response.code === 200) {
        ElMessage.success('反应路径更新成功')
      }
    } else {
      const response = await reactionPathAPI.addReactionPath(form.value)
      if (response.code === 200) {
        ElMessage.success('反应路径新增成功')
      }
    }
    showAddDialog.value = false
    editingId.value = null
    form.value = {
      fromCompoundId: null,
      toCompoundId: null,
      reactionType: '',
      energyChange: null,
      activationEnergy: null,
      description: ''
    }
    fetchPaths()
  } catch (error) {
    ElMessage.error('Save failed')
  }
}

const handleDelete = async (id) => {
  ElMessageBox.confirm('确定要删除该反应路径吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await reactionPathAPI.deleteReactionPath(id)
      if (response.code === 200) {
        ElMessage.success('反应路径删除成功')
        fetchPaths()
      }
    } catch (error) {
      ElMessage.error('Delete failed')
    }
  })
}

const handleLogout = () => {
  userStore.logout()
  ElMessage.success('退出登录成功')
  router.push('/login')
}
</script>

<style scoped>
.reaction-paths-container {
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
}

.app-sidebar {
  background-color: #f5f5f5;
  border-right: 1px solid #ddd;
}

.app-main {
  overflow-y: auto;
}

.content-wrapper {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ddd;
}

.dialog-footer {
  text-align: right;
}
</style>
