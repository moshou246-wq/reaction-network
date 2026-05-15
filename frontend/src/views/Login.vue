<template>
  <div class="login-container">
    <div class="login-box">
      <h1>{{ isLogin ? '登录' : '注册' }}</h1>
      
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label>用户名</label>
          <input v-model="form.username" type="text" placeholder="请输入用户名" required />
        </div>

        <div class="form-group">
          <label>密码</label>
          <input v-model="form.password" type="password" placeholder="请输入密码" required />
        </div>

        <div v-if="!isLogin" class="form-group">
          <label>邮箱</label>
          <input v-model="form.email" type="email" placeholder="请输入邮箱" />
        </div>

        <div v-if="!isLogin" class="form-group">
          <label>真实姓名</label>
          <input v-model="form.realName" type="text" placeholder="请输入真实姓名" />
        </div>

        <button type="submit" class="button button-primary full-width">
          {{ isLogin ? '登录' : '注册' }}
        </button>
      </form>

      <p class="toggle-text">
        {{ isLogin ? '没有账号？' : '已有账号？' }}
        <button type="button" @click="isLogin = !isLogin" class="toggle-button">
          {{ isLogin ? '去注册' : '去登录' }}
        </button>
      </p>

      <el-alert v-if="errorMessage" type="error" :title="errorMessage" :closable="true" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { userAPI } from '../api/index'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const isLogin = ref(true)
const errorMessage = ref('')
const form = ref({
  username: '',
  password: '',
  email: '',
  realName: ''
})

const handleSubmit = async () => {
  try {
    errorMessage.value = ''

    if (isLogin.value) {
      const response = await userAPI.login(form.value.username, form.value.password)
      if (response.code === 200) {
        userStore.setUserInfo(response.data)
        ElMessage.success('Login successful')
        router.push('/')
      } else {
        errorMessage.value = response.message || 'Login failed'
      }
    } else {
      const response = await userAPI.register({
        username: form.value.username,
        password: form.value.password,
        email: form.value.email,
        realName: form.value.realName
      })
      if (response.code === 200) {
        ElMessage.success('Registration successful')
        isLogin.value = true
        form.value = { username: '', password: '', email: '', realName: '' }
      } else {
        errorMessage.value = response.message || 'Registration failed'
      }
    }
  } catch (error) {
    errorMessage.value = error.message || 'Request failed'
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  padding: 40px;
  width: 100%;
  max-width: 400px;
}

h1 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
  font-size: 28px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
}

.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  transition: border-color 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.full-width {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  margin-top: 10px;
}

.toggle-text {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.toggle-button {
  background: none;
  border: none;
  color: #667eea;
  cursor: pointer;
  text-decoration: underline;
  margin-left: 5px;
}

.toggle-button:hover {
  color: #764ba2;
}
</style>
