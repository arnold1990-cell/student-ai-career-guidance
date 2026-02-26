import React from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import { clearAuth, getRoles, isLoggedIn } from '../auth/authStorage'

function navClassName({ isActive }) {
  return isActive ? 'nav-link active' : 'nav-link'
}

export default function Navbar() {
  const navigate = useNavigate()
  const loggedIn = isLoggedIn()
  const roles = getRoles()

  const handleLogout = () => {
    clearAuth()
    navigate('/')
  }

  return (
    <header className="topbar">
      <div className="brand" onClick={() => navigate('/')} role="button" tabIndex={0} onKeyDown={() => navigate('/')}>
        Edutech Career Guidance
      </div>
      <nav className="nav-links">
        <NavLink className={navClassName} to="/student/register">Student</NavLink>
        <NavLink className={navClassName} to="/company/register">Company</NavLink>
        <NavLink className={navClassName} to="/admin/login">Admin</NavLink>
      </nav>
      <div className="auth-tools">
        {loggedIn ? <span className="role-pill">{roles.join(', ')}</span> : null}
        {loggedIn ? <button onClick={handleLogout}>Logout</button> : null}
      </div>
    </header>
  )
}
