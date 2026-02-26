const ACCESS_TOKEN_KEY = 'access_token'
const REFRESH_TOKEN_KEY = 'refresh_token'
const ROLES_KEY = 'roles'
const USER_KEY = 'user'

export function setAuth({ accessToken, refreshToken, roles = [], user = null }) {
  localStorage.setItem(ACCESS_TOKEN_KEY, accessToken)
  localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken)
  localStorage.setItem(ROLES_KEY, JSON.stringify(roles))
  localStorage.setItem(USER_KEY, JSON.stringify(user))
}

export function clearAuth() {
  localStorage.removeItem(ACCESS_TOKEN_KEY)
  localStorage.removeItem(REFRESH_TOKEN_KEY)
  localStorage.removeItem(ROLES_KEY)
  localStorage.removeItem(USER_KEY)
}

export function getAccessToken() {
  return localStorage.getItem(ACCESS_TOKEN_KEY)
}

export function getRefreshToken() {
  return localStorage.getItem(REFRESH_TOKEN_KEY)
}

export function getRoles() {
  try {
    const roles = JSON.parse(localStorage.getItem(ROLES_KEY) || '[]')
    return Array.isArray(roles) ? roles : []
  } catch {
    return []
  }
}

export function getUser() {
  try {
    return JSON.parse(localStorage.getItem(USER_KEY) || 'null')
  } catch {
    return null
  }
}

export function isLoggedIn() {
  return Boolean(getAccessToken())
}

export function normalizeRole(role) {
  if (!role) return ''
  return String(role).replace('ROLE_', '').toUpperCase()
}

export function hasAnyRole(allowedRoles = []) {
  const normalizedAllowed = allowedRoles.map(normalizeRole)
  return getRoles().map(normalizeRole).some((r) => normalizedAllowed.includes(r))
}
