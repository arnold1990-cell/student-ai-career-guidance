import React from 'react'

const box = (title, detail) => <section className="card"><h2>{title}</h2><p>{detail}</p></section>

export const AdminLogin = () => box('Admin Login', 'Role-based admin authentication.')
export const ApproveBursaries = () => box('Approve Bursaries', 'Review, approve, or reject bursary submissions.')
export const ManageUsers = () => box('Manage Users', 'Manage users, roles, and bulk CSV imports.')
export const AnalyticsPage = () => box('Analytics', 'User growth and subscription/payment tracking dashboard.')
