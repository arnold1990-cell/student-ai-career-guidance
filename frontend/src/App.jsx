import React from 'react'
import { Navigate, Route, Routes } from 'react-router-dom'
import RequireAuth from './auth/RequireAuth'
import Navbar from './components/Navbar'
import RouterDebug from './components/RouterDebug'
import HomePage from './pages/HomePage'
import StudentLogin from './pages/StudentLogin'
import CompanyLogin from './pages/CompanyLogin'
import AdminLogin from './pages/AdminLogin'
import StudentDashboard from './pages/StudentDashboard'
import CompanyDashboard from './pages/CompanyDashboard'
import AdminDashboard from './pages/AdminDashboard'
import UnauthorizedPage from './pages/UnauthorizedPage'
import NotFoundPage from './pages/NotFoundPage'

export default function App() {
  return (
    <div className="app-shell">
      <Navbar />
      <main className="main-content">
        <RouterDebug />
        <Routes>
          <Route path="/" element={<HomePage />} />

          <Route path="/student/login" element={<StudentLogin />} />
          <Route path="/company/login" element={<CompanyLogin />} />
          <Route path="/admin/login" element={<AdminLogin />} />

          <Route path="/student" element={<Navigate to="/student/dashboard" replace />} />
          <Route
            path="/student/dashboard"
            element={(
              <RequireAuth allowedRoles={['STUDENT']} redirectTo="/student/login">
                <StudentDashboard />
              </RequireAuth>
            )}
          />

          <Route path="/company" element={<Navigate to="/company/dashboard" replace />} />
          <Route
            path="/company/dashboard"
            element={(
              <RequireAuth allowedRoles={['COMPANY']} redirectTo="/company/login">
                <CompanyDashboard />
              </RequireAuth>
            )}
          />

          <Route path="/admin" element={<Navigate to="/admin/dashboard" replace />} />
          <Route
            path="/admin/dashboard"
            element={(
              <RequireAuth allowedRoles={['ADMIN']} redirectTo="/admin/login">
                <AdminDashboard />
              </RequireAuth>
            )}
          />

          <Route path="/unauthorized" element={<UnauthorizedPage />} />
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </main>
    </div>
  )
}
