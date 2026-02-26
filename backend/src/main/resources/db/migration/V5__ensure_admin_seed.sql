INSERT INTO roles (name)
VALUES ('ADMIN')
ON CONFLICT (name) DO NOTHING;

INSERT INTO users (email, password_hash, enabled, created_at, updated_at)
SELECT 'admin@edutech.local', '$2b$12$.GPGeD6IDcshmdhjofMfceek5LEojkQXIl8XxgEmbJH4PXXiJ0xVG', TRUE, NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE LOWER(email) = 'admin@edutech.local'
);

UPDATE users
SET password_hash = '$2b$12$.GPGeD6IDcshmdhjofMfceek5LEojkQXIl8XxgEmbJH4PXXiJ0xVG',
    enabled = TRUE,
    updated_at = NOW()
WHERE LOWER(email) = 'admin@edutech.local';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ADMIN'
WHERE LOWER(u.email) = 'admin@edutech.local'
ON CONFLICT (user_id, role_id) DO NOTHING;
