<template>
  <div class="compounds-container">
    <el-header class="app-header">
      <div class="header-left">
        <h1 class="app-title">化合物管理</h1>
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
            <h2>化合物</h2>
            <el-button type="primary" @click="showAddDialog = true">
              + 新增化合物
            </el-button>
          </div>

          <el-card class="search-card">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索化合物..."
              @input="handleSearch"
            >
              <template #prefix>
                <i class="el-icon-search"></i>
              </template>
            </el-input>
          </el-card>

          <el-table
            :data="compounds"
            stripe
            style="width: 100%"
          >
            <el-table-column prop="id" label="ID" width="80"></el-table-column>
            <el-table-column prop="name" label="名称"></el-table-column>
            <el-table-column prop="formula" label="分子式"></el-table-column>
            <el-table-column prop="molarMass" label="摩尔质量"></el-table-column>
            <el-table-column prop="energy" label="能量"></el-table-column>
            <el-table-column label="操作" width="200">
              <template #default="{ row }">
                <el-button link type="primary" @click="handleEdit(row)">Edit</el-button>
                <el-button link type="danger" @click="handleDelete(row.id)">Delete</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- Add/Edit Dialog -->
          <el-dialog
            v-model="showAddDialog"
            :title="editingId ? '编辑化合物' : '新增化合物'"
          >
            <el-form :model="form" label-width="120px">
              <el-form-item label="名称">
                <el-input v-model="form.name"></el-input>
              </el-form-item>
              <el-form-item label="分子式">
                <el-input v-model="form.formula"></el-input>
              </el-form-item>
              <el-form-item label="摩尔质量">
                <el-input-number v-model="form.molarMass"></el-input-number>
              </el-form-item>
              <el-form-item label="能量">
                <el-input-number v-model="form.energy"></el-input-number>
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
import { compoundAPI } from '../api/index'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const activeMenu = ref('/compounds')
const compounds = ref([])
const searchKeyword = ref('')
const showAddDialog = ref(false)
const editingId = ref(null)

const form = ref({
  name: '',
  formula: '',
  molarMass: null,
  energy: null,
  description: ''
})

onMounted(() => {
  fetchCompounds()
})

const fetchCompounds = async () => {
  try {
    const response = await compoundAPI.getAllCompounds()
    if (response.code === 200) {
      compounds.value = response.data
    }
  } catch (error) {
    ElMessage.error('获取化合物失败')
  }
}

const handleSearch = async () => {
  if (searchKeyword.value) {
    try {
      const response = await compoundAPI.searchCompounds(searchKeyword.value)
      if (response.code === 200) {
        compounds.value = response.data
      }
    } catch (error) {
      ElMessage.error('Search failed')
    }
  } else {
    fetchCompounds()
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
      const response = await compoundAPI.updateCompound(editingId.value, form.value)
      if (response.code === 200) {
        ElMessage.success('化合物更新成功')
      }
    } else {
      const response = await compoundAPI.addCompound(form.value)
      if (response.code === 200) {
        ElMessage.success('化合物新增成功')
      }
    }
    showAddDialog.value = false
    editingId.value = null
    form.value = { name: '', formula: '', molarMass: null, energy: null, description: '' }
    fetchCompounds()
  } catch (error) {
    ElMessage.error('Save failed')
  }
}

const handleDelete = async (id) => {
  ElMessageBox.confirm('确定要删除该化合物吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await compoundAPI.deleteCompound(id)
      if (response.code === 200) {
        ElMessage.success('化合物删除成功')
        fetchCompounds()
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
.compounds-container {
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

.header-left {
  display: flex;
  align-items: center;
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

.search-card {
  margin-bottom: 20px;
}

.dialog-footer {
  text-align: right;
}
</style>
