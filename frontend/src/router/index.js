import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Dashboard from '../views/Dashboard.vue'
import Compounds from '../views/Compounds.vue'
import ReactionPaths from '../views/ReactionPaths.vue'
import Visualization from '../views/Visualization.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    name: 'Dashboard',
    component: Dashboard,
    meta: { requiresAuth: true }
  },
  {
    path: '/compounds',
    name: 'Compounds',
    component: Compounds,
    meta: { requiresAuth: true }
  },
  {
    path: '/reaction-paths',
    name: 'ReactionPaths',
    component: ReactionPaths,
    meta: { requiresAuth: true }
  },
  {
    path: '/visualization',
    name: 'Visualization',
    component: Visualization,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
