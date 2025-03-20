-- Enable UUID generation extension
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 1. USERS TABLE (only UUIDs, no personal data)
CREATE TABLE users
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid()
);

-- 2. AUTHENTICATORS TABLE
CREATE TABLE authenticators
(
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name      VARCHAR(50) UNIQUE NOT NULL,
    auth_type VARCHAR(20)        NOT NULL,
    config    JSONB              NOT NULL,
    is_active BOOLEAN          DEFAULT TRUE
);

-- 3. USER AUTH MAPPINGS TABLE
CREATE TABLE user_auth_mappings
(
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id          UUID         NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    authenticator_id UUID         NOT NULL REFERENCES authenticators (id) ON DELETE CASCADE,
    external_user_id VARCHAR(100) NOT NULL,
    UNIQUE (authenticator_id, external_user_id)
);

-- 4. APPLICATIONS TABLE
CREATE TABLE applications
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name            VARCHAR(100) UNIQUE NOT NULL,
    description     TEXT,
    default_role_id UUID REFERENCES roles (id)
);

-- 5. ROLES TABLE
CREATE TABLE roles
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

-- 6. PERMISSIONS TABLE
CREATE TABLE permissions
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

-- 7. PERMISSIONS_ROLE TABLE (role -> permission mapping)
CREATE TABLE permissions_role
(
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    role_id       UUID NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
    permission_id UUID NOT NULL REFERENCES permissions (id) ON DELETE CASCADE,
    UNIQUE (role_id, permission_id)
);

-- 8. APPLICATION_ROLES TABLE (roles valid for each app)
CREATE TABLE application_roles
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    application_id UUID NOT NULL REFERENCES applications (id) ON DELETE CASCADE,
    role_id        UUID NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
    UNIQUE (application_id, role_id)
);

-- 9. USERS_ROLES_APPLICATION TABLE (user-role-app mapping, multiple roles allowed)
CREATE TABLE users_roles_application
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id        UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    application_id UUID NOT NULL REFERENCES applications (id) ON DELETE CASCADE,
    role_id        UUID NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
    UNIQUE (user_id, application_id, role_id)
);

-- 10. APPLICATION_AUTHENTICATORS TABLE (which authenticator each app supports + order)
CREATE TABLE application_authenticators
(
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    application_id   UUID NOT NULL REFERENCES applications (id) ON DELETE CASCADE,
    authenticator_id UUID NOT NULL REFERENCES authenticators (id) ON DELETE CASCADE,
    display_order    INT  NOT NULL    DEFAULT 0,
    is_active        BOOLEAN          DEFAULT TRUE,
    config           JSONB,
    UNIQUE (application_id, authenticator_id)
);

-- 11. AUDIT_EVENT_TYPE TABLE
CREATE TABLE audit_event_type
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    description VARCHAR(50) NOT NULL
);

-- 12. AUDIT_LOGS TABLE
CREATE TABLE audit_logs
(
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    timestamp        TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    event_type       UUID REFERENCES audit_event_type (id) ON DELETE SET NULL,
    user_id          UUID REFERENCES users (id) ON DELETE SET NULL,
    application_name VARCHAR(100),
    description      TEXT,
    metadata         JSONB
);
