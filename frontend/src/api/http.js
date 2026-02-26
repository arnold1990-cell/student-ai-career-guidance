import { clearAuth, getAccessToken, getRefreshToken, setAuth } from '../auth/authStorage'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? ''
const IS_DEV = import.meta.env.DEV
let refreshPromise = null

function buildUrl(path) {
  if (path.startsWith('http')) {
    return path
  }

  return `${API_BASE_URL}${path}`
}

async function parseResponseBody(response) {
  const contentType = response.headers.get('content-type') || ''
  const raw = await response.text()

  if (!raw) {
    return null
  }

  if (contentType.includes('application/json')) {
    try {
      return JSON.parse(raw)
    } catch {
      return { raw }
    }
  }

  return { raw }
}

function buildHttpError(response, payload) {
  const message =
    payload?.message ||
    payload?.error ||
    payload?.raw ||
    `Request failed with status ${response.status}`

  return {
    status: response.status,
    message,
    details: payload
  }
}

function buildNetworkError(error) {
  const message =
    error?.message === 'Failed to fetch'
      ? 'Network error: unable to reach the server. Ensure backend is running and API URL/proxy is correct.'
      : error?.message || 'Network error while contacting server.'

  return {
    status: null,
    message,
    details: error
  }
}

function maybeLogError(error, context) {
  if (IS_DEV) {
    console.error(`[api] ${context}`, error)
  }
}

async function rawRequest(path, options = {}) {
  const headers = new Headers(options.headers || {})

  if (!headers.has('Content-Type') && options.body && !(options.body instanceof FormData)) {
    headers.set('Content-Type', 'application/json')
  }

  const token = getAccessToken()
  if (token && !headers.has('Authorization')) {
    headers.set('Authorization', `Bearer ${token}`)
  }

  try {
    return await fetch(buildUrl(path), { ...options, headers })
  } catch (error) {
    const networkError = buildNetworkError(error)
    maybeLogError(networkError, `Network failure for ${path}`)
    throw networkError
  }
}

async function attemptRefresh() {
  if (refreshPromise) {
    return refreshPromise
  }

  const refreshToken = getRefreshToken()
  if (!refreshToken) {
    return false
  }

  refreshPromise = rawRequest('/api/auth/refresh', {
    method: 'POST',
    body: JSON.stringify({ refreshToken })
  })
    .then(async (response) => {
      const payload = await parseResponseBody(response)
      if (!response.ok) {
        throw buildHttpError(response, payload)
      }

      const currentRoles = JSON.parse(localStorage.getItem('roles') || '[]')
      const currentUser = JSON.parse(localStorage.getItem('user') || 'null')

      setAuth({
        accessToken: payload.accessToken,
        refreshToken: payload.refreshToken,
        roles: currentRoles,
        user: currentUser
      })

      return true
    })
    .catch((error) => {
      maybeLogError(error, 'Refresh token flow failed')
      clearAuth()
      return false
    })
    .finally(() => {
      refreshPromise = null
    })

  return refreshPromise
}

export async function apiFetch(path, options = {}, retry = true) {
  const response = await rawRequest(path, options)

  if (response.status === 401 && retry) {
    const refreshed = await attemptRefresh()

    if (refreshed) {
      return apiFetch(path, options, false)
    }

    clearAuth()

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
  const payload = await parseResponseBody(response)

  if (!response.ok) {
    const httpError = buildHttpError(response, payload)
    maybeLogError(httpError, `HTTP failure for ${path}`)
    throw httpError
  }

  return payload
}
