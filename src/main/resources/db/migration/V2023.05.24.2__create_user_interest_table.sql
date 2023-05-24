-- Set search path to user_service_schema
SET search_path TO user_service_schema;

-- Create the "user_interest" table
CREATE TABLE user_interest
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES user_service_schema."user" (id),
    interest   VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
