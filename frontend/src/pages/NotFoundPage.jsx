import React from 'react'
import { Link } from 'react-router-dom'

export default function NotFoundPage() {
  return (
    <section className="card">
      <h2>Page Not Found</h2>
      <p>The page you requested does not exist.</p>
      <Link className="entry-btn" to="/">Go to Home</Link>
    </section>
  )
}
