import React from 'react'

const box = (title, detail) => <section className="card"><h2>{title}</h2><p>{detail}</p></section>

export const CompanyAuth = () => box('Company Register/Login', 'Register bursary providers with verification upload support.')
export const CompanyDashboard = () => box('Company Dashboard', 'Monitor bursary pipeline and engagement analytics.')
export const PostBursary = () => box('Post Bursary', 'Create bursaries for admin approval workflow.')
export const TalentSearch = () => box('Talent Search', 'Find students by subjects, grades, field of study, and location.')
