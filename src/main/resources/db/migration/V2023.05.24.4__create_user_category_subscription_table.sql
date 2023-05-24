-- Set search path to user_service_schema
SET search_path TO user_service_schema;

-- Create the "user_category_subscription" table
CREATE TABLE user_category_subscription (
                                            id BIGSERIAL PRIMARY KEY,
                                            user_id BIGINT REFERENCES user_service_schema."user"(id),
                                            category_id BIGINT,
                                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
