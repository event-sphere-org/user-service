ALTER TABLE user_service_schema.user_event_subscription
    ADD CONSTRAINT unique_user_event_subscription UNIQUE (user_id, event_id);
