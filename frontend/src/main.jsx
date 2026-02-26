import React from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Link, Route, Routes } from 'react-router-dom'
import './styles.css'
import { StudentDashboard, StudentAuth, CareerExplorer, BursaryFinder, SubscriptionPage } from './pages/student'
import { CompanyAuth, CompanyDashboard, PostBursary, TalentSearch } from './pages/company'
import { AdminLogin, ApproveBursaries, ManageUsers, AnalyticsPage } from './pages/admin'

function Layout() {
  return (
    <div className="app">
      <header>
        <h1>Edutech AI Career Guidance Platform</h1>
        <nav>
          <Link to="/student/dashboard">Student</Link>
          <Link to="/company/dashboard">Company</Link>
          <Link to="/admin/analytics">Admin</Link>
        </nav>
      </header>
      <main>
        <Routes>
          <Route path="/student/login" element={<StudentAuth />} />
          <Route path="/student/dashboard" element={<StudentDashboard />} />
          <Route path="/student/careers" element={<CareerExplorer />} />
          <Route path="/student/bursaries" element={<BursaryFinder />} />
          <Route path="/student/subscription" element={<SubscriptionPage />} />

          <Route path="/company/login" element={<CompanyAuth />} />
          <Route path="/company/dashboard" element={<CompanyDashboard />} />
          <Route path="/company/post-bursary" element={<PostBursary />} />
          <Route path="/company/talent-search" element={<TalentSearch />} />

          <Route path="/admin/login" element={<AdminLogin />} />
          <Route path="/admin/approve-bursaries" element={<ApproveBursaries />} />
          <Route path="/admin/manage-users" element={<ManageUsers />} />
          <Route path="/admin/analytics" element={<AnalyticsPage />} />
          <Route path="*" element={<StudentDashboard />} />
        </Routes>
      </main>
    </div>
  )
}

createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      <Layout />
    </BrowserRouter>
  </React.StrictMode>
)
