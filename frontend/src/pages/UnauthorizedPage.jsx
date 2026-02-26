import React from 'react'
import { Link } from 'react-router-dom'

export default function UnauthorizedPage() {
  return (
    <section className="card">
      <h2>Unauthorized</h2>
      <p>You are logged in but do not have permission to view this page.</p>
      <Link className="entry-btn" to="/">Back Home</Link>
    </section>
  )
}
