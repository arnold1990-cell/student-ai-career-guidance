CREATE TABLE IF NOT EXISTS student_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    full_name VARCHAR(255) NOT NULL,
    subjects TEXT NOT NULL,
    grades TEXT NOT NULL,
    field_of_study VARCHAR(255) NOT NULL,
    location VARCHAR(120) NOT NULL,
    cv_url VARCHAR(255),
    transcript_url VARCHAR(255),
    qualifications TEXT,
    experience TEXT
);

CREATE TABLE IF NOT EXISTS company_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    company_name VARCHAR(255) NOT NULL,
    website VARCHAR(255),
    industry VARCHAR(120),
    verification_document_url VARCHAR(255)
);

ALTER TABLE bursaries
    ALTER COLUMN status TYPE VARCHAR(32);

ALTER TABLE bursaries
    ALTER COLUMN status SET DEFAULT 'PENDING_APPROVAL';

ALTER TABLE bursaries
    ADD COLUMN IF NOT EXISTS field_of_study VARCHAR(255) NOT NULL DEFAULT 'General',
    ADD COLUMN IF NOT EXISTS location VARCHAR(120) NOT NULL DEFAULT 'Online',
    ADD COLUMN IF NOT EXISTS eligibility TEXT NOT NULL DEFAULT 'Open';

CREATE TABLE IF NOT EXISTS bursary_applications (
    id BIGSERIAL PRIMARY KEY,
    bursary_id BIGINT NOT NULL REFERENCES bursaries(id) ON DELETE CASCADE,
    student_id BIGINT NOT NULL REFERENCES student_profiles(id) ON DELETE CASCADE,
    status VARCHAR(32) NOT NULL DEFAULT 'SUBMITTED',
    applied_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS bursary_bookmarks (
    id BIGSERIAL PRIMARY KEY,
    bursary_id BIGINT NOT NULL REFERENCES bursaries(id) ON DELETE CASCADE,
    student_id BIGINT NOT NULL REFERENCES student_profiles(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (bursary_id, student_id)
);

CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    subscription_id BIGINT NOT NULL REFERENCES subscriptions(id) ON DELETE CASCADE,
    amount NUMERIC(12,2) NOT NULL,
    provider VARCHAR(64) NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

INSERT INTO roles(name) VALUES ('STUDENT') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles(name) VALUES ('COMPANY') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles(name) VALUES ('ADMIN') ON CONFLICT (name) DO NOTHING;

INSERT INTO users(email, password_hash, enabled)
VALUES ('admin@edutech.local', '$2a$10$7EqJtq98hPqEX7fNZaFWoOq6QfH6R1QO5jM90oCbGyF/F7fs/3Gz.', true)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles(user_id, role_id)
SELECT u.id, r.id FROM users u JOIN roles r ON r.name='ADMIN'
WHERE u.email='admin@edutech.local'
ON CONFLICT (user_id, role_id) DO NOTHING;
