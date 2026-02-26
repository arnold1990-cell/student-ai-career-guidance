import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { apiJson } from '../api/http'

const roleConfig = {
  STUDENT: {
    endpoint: '/api/auth/register/student',
    loginPath: '/student/login',
    fields: [
      { name: 'firstName', label: 'First name', required: true },
      { name: 'lastName', label: 'Last name', required: true }
    ]
  },
  COMPANY: {
    endpoint: '/api/auth/register/company',
    loginPath: '/company/login',
    fields: [
      { name: 'companyName', label: 'Company name', required: true }
    ]
  }
}

export default function RegisterForm({ role }) {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [form, setForm] = useState(() => {
    const base = { email: '', password: '' }
    roleConfig[role].fields.forEach((field) => {
      base[field.name] = ''
    })
    return base
  })

  const onChange = (event) => {
    const { name, value } = event.target
    setForm((current) => ({ ...current, [name]: value }))
  }

  const onSubmit = async (event) => {
    event.preventDefault()
    setLoading(true)
    setError('')
    setSuccess('')

    try {
      await apiJson(roleConfig[role].endpoint, {
        method: 'POST',
        body: JSON.stringify(form)
      })
      setSuccess('Registration successful. Redirecting to login...')
      setTimeout(() => navigate(roleConfig[role].loginPath, { replace: true }), 700)
    } catch (submissionError) {
      setError(submissionError?.message || 'Registration failed')
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="card login-card">
      <h2>{role} Registration</h2>
      <form onSubmit={onSubmit} className="login-form">
        <label>
          Email
          <input type="email" name="email" value={form.email} onChange={onChange} required />
        </label>

        <label>
          Password
          <input type="password" name="password" value={form.password} onChange={onChange} minLength={8} required />
        </label>

        {roleConfig[role].fields.map((field) => (
          <label key={field.name}>
            {field.label}
            <input type="text" name={field.name} value={form[field.name]} onChange={onChange} required={field.required} />
          </label>
        ))}

        {error ? <p className="error-msg">{error}</p> : null}
        {success ? <p className="success-msg">{success}</p> : null}
        <button type="submit" disabled={loading}>{loading ? 'Registering...' : 'Register'}</button>
      </form>
    </section>
  )
}
