import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { apiJson } from '../api/http'
import { setAuth, normalizeRole } from '../auth/authStorage'

const roleConfig = {
  STUDENT: { loginPath: '/student/login', dashboardPath: '/student/dashboard', registerPath: '/student/register' },
  COMPANY: { loginPath: '/company/login', dashboardPath: '/company/dashboard', registerPath: '/company/register' },
  ADMIN: { loginPath: '/admin/login', dashboardPath: '/admin/dashboard' }
}

function formatSubmissionError(submissionError) {
  if (!submissionError) {
    return 'Login failed'
  }

  if (submissionError.status === 401) {
    return 'Invalid credentials'
  }

  if (submissionError.status) {
    return `Login failed (${submissionError.status}): ${submissionError.message}`
  }

  return submissionError.message || 'Login failed'
}

export default function LoginForm({ role }) {
  const navigate = useNavigate()
  const [form, setForm] = useState({ email: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const onChange = (event) => {
    const { name, value } = event.target
    setForm((current) => ({ ...current, [name]: value }))
  }

  const onSubmit = async (event) => {
    event.preventDefault()
    setError('')
    setLoading(true)

    try {
      const tokenData = await apiJson('/api/auth/login', {
        method: 'POST',
        body: JSON.stringify(form)
      })

      const roles = Array.isArray(tokenData.roles) ? tokenData.roles : []
      const normalizedRoles = roles.map(normalizeRole)
      if (!normalizedRoles.includes(normalizeRole(role))) {
        throw { status: 403, message: `This account does not have ${role} access.` }
      }

      setAuth({
        accessToken: tokenData.accessToken,
        refreshToken: tokenData.refreshToken,
        roles,
        user: { email: tokenData.email ?? form.email }
      })

      navigate(roleConfig[role].dashboardPath, { replace: true })
    } catch (submissionError) {
      setError(formatSubmissionError(submissionError))
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="card login-card">
      <h2>{role} Login</h2>
      <form onSubmit={onSubmit} className="login-form">
        <label>
          Email
          <input type="email" name="email" value={form.email} onChange={onChange} required />
        </label>

        <label>
          Password
          <input type="password" name="password" value={form.password} onChange={onChange} required minLength={8} />
        </label>

        {error ? <p className="error-msg">{error}</p> : null}
        <button type="submit" disabled={loading}>{loading ? 'Signing in...' : 'Login'}</button>
      </form>
      {role !== 'ADMIN' ? <p className="helper">No account? <Link to={roleConfig[role].registerPath}>Register first</Link>.</p> : null}
      <p className="helper">Need another portal? Use the navbar to switch between role logins.</p>
    </section>
  )
}
