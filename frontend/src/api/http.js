import { clearAuth, getAccessToken, getRefreshToken, setAuth } from '../auth/authStorage'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
let refreshPromise = null

async function attemptRefresh() {
  if (refreshPromise) {
    return refreshPromise
  }

  const refreshToken = getRefreshToken()
  if (!refreshToken) {
    return false
  }

  refreshPromise = fetch(`${API_BASE_URL}/api/auth/refresh`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ refreshToken })
  })
    .then(async (response) => {
      if (!response.ok) {
        throw new Error('Refresh failed')
      }

      const data = await response.json()
      const currentRoles = JSON.parse(localStorage.getItem('roles') || '[]')
      const currentUser = JSON.parse(localStorage.getItem('user') || 'null')

      setAuth({
        accessToken: data.accessToken,
        refreshToken: data.refreshToken,
        roles: currentRoles,
        user: currentUser
      })

      return true
    })
    .catch(() => {
      clearAuth()
      return false
    })
    .finally(() => {
      refreshPromise = null
    })

  return refreshPromise
}

export async function apiFetch(path, options = {}, retry = true) {
  const url = path.startsWith('http') ? path : `${API_BASE_URL}${path}`
  const headers = new Headers(options.headers || {})

  if (!headers.has('Content-Type') && options.body && !(options.body instanceof FormData)) {
    headers.set('Content-Type', 'application/json')
  }

  const token = getAccessToken()
  if (token) {
    headers.set('Authorization', `Bearer ${token}`)
  }

  const response = await fetch(url, { ...options, headers })

  if (response.status === 401 && retry) {
    const refreshed = await attemptRefresh()

    if (refreshed) {
      return apiFetch(path, options, false)
    }

    if (typeof window !== 'undefined') {
      const section = window.location.pathname.split('/')[1]
      const redirectMap = {
        student: '/student/login',
        company: '/company/login',
        admin: '/admin/login'
      }
      window.location.assign(redirectMap[section] || '/')
    }
  }

  return response
}

export async function apiJson(path, options = {}, retry = true) {
  const response = await apiFetch(path, options, retry)
  const text = await response.text()
  const payload = text ? JSON.parse(text) : null

  if (!response.ok) {
    const message = payload?.message || payload?.error || `HTTP ${response.status}`
    throw new Error(message)
  }

  return payload
}
