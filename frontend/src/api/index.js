import apiClient from './request'

// 用户API
export const userAPI = {
  login(username, password) {
    return apiClient.post('/users/login', { username, password })
  },
  register(user) {
    return apiClient.post('/users/register', user)
  },
  getUserInfo(userId) {
    return apiClient.get(`/users/${userId}`)
  }
}

// 化合物API
export const compoundAPI = {
  getAllCompounds() {
    return apiClient.get('/compounds')
  },
  searchCompounds(keyword) {
    return apiClient.get('/compounds/search', { params: { keyword } })
  },
  getCompound(id) {
    return apiClient.get(`/compounds/${id}`)
  },
  addCompound(compound) {
    return apiClient.post('/compounds', compound)
  },
  updateCompound(id, compound) {
    return apiClient.put(`/compounds/${id}`, compound)
  },
  deleteCompound(id) {
    return apiClient.delete(`/compounds/${id}`)
  }
}

// 反应路径API
export const reactionPathAPI = {
  getAllReactionPaths() {
    return apiClient.get('/reaction-paths')
  },
  getReactionPath(id) {
    return apiClient.get(`/reaction-paths/${id}`)
  },
  addReactionPath(path) {
    return apiClient.post('/reaction-paths', path)
  },
  updateReactionPath(id, path) {
    return apiClient.put(`/reaction-paths/${id}`, path)
  },
  deleteReactionPath(id) {
    return apiClient.delete(`/reaction-paths/${id}`)
  },
  findShortestPath(startId, endId) {
    return apiClient.get('/reaction-paths/shortest-path', {
      params: { startId, endId }
    })
  },
  findLowestEnergyPath(startId, endId) {
    return apiClient.get('/reaction-paths/lowest-energy-path', {
      params: { startId, endId }
    })
  }
}
