-- Set search path to user_service_schema
SET search_path TO user_service_schema;

-- Create the "user" table
CREATE TABLE "user"
(
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(50) UNIQUE  NOT NULL,
    password      VARCHAR(255)        NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    first_name    VARCHAR(50),
    last_name     VARCHAR(50),
    date_of_birth DATE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
