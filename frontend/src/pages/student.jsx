import React from 'react'

const box = (title, detail) => <section className="card"><h2>{title}</h2><p>{detail}</p></section>

export const StudentAuth = () => box('Student Register/Login', 'Secure access with JWT authentication.')
export const StudentDashboard = () => box('Student Dashboard', 'Track profile, applications, recommendations, and alerts.')
export const CareerExplorer = () => box('Career Explorer', 'AI-powered top 5 career recommendations and skill gaps.')
export const BursaryFinder = () => box('Bursary Finder', 'Search, bookmark, and apply for bursaries.')
export const SubscriptionPage = () => box('Subscription', 'Upgrade to PREMIUM for unlimited recommendations and insights.')
