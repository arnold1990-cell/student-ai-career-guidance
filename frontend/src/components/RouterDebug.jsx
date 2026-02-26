import React from 'react'
import { useLocation } from 'react-router-dom'
import { getRoles, isLoggedIn } from '../auth/authStorage'

export default function RouterDebug() {
  const location = useLocation()

  if (!import.meta.env.DEV) {
    return null
  }

  return (
    <div className="router-debug">
      <strong>Debug:</strong> {location.pathname} | loggedIn: {String(isLoggedIn())} | roles: {getRoles().join(', ') || 'none'}
    </div>
  )
}
