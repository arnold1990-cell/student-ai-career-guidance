import React from 'react'
import { Link } from 'react-router-dom'

export default function HomePage() {
  return (
    <section className="card">
      <h2>Welcome</h2>
      <p>Select your portal to continue.</p>
      <div className="entry-grid">
        <Link className="entry-btn" to="/student/register">Student Register</Link>
        <Link className="entry-btn" to="/student/login">Student Login</Link>
        <Link className="entry-btn" to="/company/register">Company Register</Link>
        <Link className="entry-btn" to="/company/login">Company Login</Link>
        <Link className="entry-btn" to="/admin/login">Admin Login</Link>
      </div>
    </section>
  )
}
