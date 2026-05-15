import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userId = ref(localStorage.getItem('userId') || '')
  const username = ref(localStorage.getItem('username') || '')
  const email = ref(localStorage.getItem('email') || '')

  const isLoggedIn = computed(() => !!token.value)

  const setUserInfo = (userInfo) => {
    token.value = userInfo.token
    userId.value = userInfo.userId
    username.value = userInfo.username
    email.value = userInfo.email

    localStorage.setItem('token', userInfo.token)
    localStorage.setItem('userId', userInfo.userId)
    localStorage.setItem('username', userInfo.username)
    localStorage.setItem('email', userInfo.email)
  }

  const logout = () => {
    token.value = ''
    userId.value = ''
    username.value = ''
    email.value = ''

    localStorage.removeItem('token')
    localStorage.removeItem('userId')
    localStorage.removeItem('username')
    localStorage.removeItem('email')
  }

  return {
    token,
    userId,
    username,
    email,
    isLoggedIn,
    setUserInfo,
    logout
  }
})
