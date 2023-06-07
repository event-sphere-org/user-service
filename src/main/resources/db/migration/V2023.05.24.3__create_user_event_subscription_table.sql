-- Set search path to user_service_schema
SET search_path TO user_service_schema;

-- Create the "user_event_subscription" table
CREATE TABLE user_event_subscription
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES user_service_schema."user" (id),
    event_id   BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);