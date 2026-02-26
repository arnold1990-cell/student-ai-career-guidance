CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO roles (name)
VALUES ('ADMIN'), ('STUDENT'), ('COMPANY')
ON CONFLICT (name) DO NOTHING;

INSERT INTO users (email, password_hash, enabled, created_at, updated_at)
SELECT 'admin@edutech.local', crypt('Admin@12345', gen_salt('bf')), TRUE, NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'admin@edutech.local'
);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.name = 'ADMIN'
WHERE u.email = 'admin@edutech.local'
ON CONFLICT (user_id, role_id) DO NOTHING;
