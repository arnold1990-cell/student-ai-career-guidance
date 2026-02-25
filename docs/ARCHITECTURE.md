# Edutech Architecture

## 1. Web Application Architecture

Edutech follows a monorepo, web-first architecture:

- **Frontend (`frontend/`)**: React single-page application for Student, Company, and Admin portals.
- **Backend (`backend/`)**: Spring Boot API services for authentication, recommendations, opportunities, applications, billing, and notifications.
- **Data Layer**: Relational database named **`edutech`** for transactional data.
- **Integration Layer**: External providers for AI inference, payments, and communications.

## 2. Roles and Access

Role-based access control is enforced across all API endpoints and UI routes:

- **Student**: profile management, recommendations, applications.
- **Company**: opportunity publishing, applicant review.
- **Admin**: user governance, analytics, platform operations.

## 3. Authentication and Security

- Token-based authentication (e.g., JWT).
- Secure password policies and optional multi-factor authentication.
- Audit logging for sensitive actions.
- Confidential data handled according to documented privacy policies.

## 4. Backend Design

Backend code uses Java package base **`com.edutech`** and is organized by domain modules:

- `auth`
- `student`
- `company`
- `admin`
- `recommendation`
- `bursary`
- `subscription`
- `notification`

Environment variables should use the **`EDUTECH_`** prefix (for example: `EDUTECH_DB_NAME`, `EDUTECH_API_BASE_URL`).

## 5. Database

Primary relational database: **`edutech`**

Example entities include:

- users
- student_profiles
- company_profiles
- opportunities
- applications
- recommendations
- subscriptions
- notifications

## 6. Integrations

- **AI Provider**: recommendation and ranking workflows.
- **Payment Gateway**: recurring subscription collection and status webhooks.
- **Communication Provider**: email/SMS/push notifications.

## 7. Deployment Model

- Containerized services for frontend and backend.
- Environment-specific configuration via `EDUTECH_` variables.
- CI/CD pipeline for build, test, and deployment automation.
- Production readiness includes observability (logs, metrics, alerts).

## 8. Future Mobile Scalability

Architecture is API-first to support future native and hybrid mobile applications:

- Reuse backend APIs across web and mobile clients.
- Introduce mobile-focused auth/session flows as needed.
- Maintain feature parity through shared domain services and contracts.
