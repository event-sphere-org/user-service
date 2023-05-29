-- Insert meaningful data into the 'user' table
INSERT INTO user_service_schema."user" (username, password, email, first_name, last_name, date_of_birth, created_at, updated_at)
VALUES
    ('john_doe', 'password123', 'john.doe@example.com', 'John', 'Doe', '1990-01-01', current_timestamp, current_timestamp),
    ('jane_smith', 'secret456', 'jane.smith@example.com', 'Jane', 'Smith', '1992-05-15', current_timestamp, current_timestamp),
    ('mike_jackson', 'qwerty789', 'mike.jackson@example.com', 'Mike', 'Jackson', '1985-09-30', current_timestamp, current_timestamp),
    ('alice_walker', 'abc123', 'alice.walker@example.com', 'Alice', 'Walker', '1991-12-10', current_timestamp, current_timestamp),
    ('peter_wilson', 'password321', 'peter.wilson@example.com', 'Peter', 'Wilson', '1988-06-20', current_timestamp, current_timestamp),
    ('sara_johnson', 'sara123', 'sara.johnson@example.com', 'Sara', 'Johnson', '1993-03-08', current_timestamp, current_timestamp),
    ('david_smith', 'david456', 'david.smith@example.com', 'David', 'Smith', '1987-11-25', current_timestamp, current_timestamp),
    ('emily_davis', 'emily789', 'emily.davis@example.com', 'Emily', 'Davis', '1994-08-12', current_timestamp, current_timestamp),
    ('robert_wilson', 'robertabc', 'robert.wilson@example.com', 'Robert', 'Wilson', '1990-04-05', current_timestamp, current_timestamp),
    ('linda_adams', 'linda123', 'linda.adams@example.com', 'Linda', 'Adams', '1989-02-17', current_timestamp, current_timestamp);