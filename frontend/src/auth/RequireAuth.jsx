import React from 'react'
import { Navigate } from 'react-router-dom'
import { hasAnyRole, isLoggedIn } from './authStorage'

export default function RequireAuth({ allowedRoles, redirectTo, children }) {
  if (!isLoggedIn()) {
    return <Navigate to={redirectTo} replace />
  }

  if (!hasAnyRole(allowedRoles)) {
    return <Navigate to="/unauthorized" replace />
  }

  return children
}
