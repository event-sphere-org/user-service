CREATE TABLE "user" (
                        id BIGSERIAL PRIMARY KEY,
                        username VARCHAR(50) UNIQUE NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        email VARCHAR(100) UNIQUE NOT NULL,
                        first_name VARCHAR(50),
                        last_name VARCHAR(50),
                        date_of_birth DATE,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "event" (
                         id BIGSERIAL PRIMARY KEY,
                         creator_id BIGINT REFERENCES "user"(id),
                         category_id BIGINT,
                         title VARCHAR(100) NOT NULL,
                         description VARCHAR(500),
                         image_url VARCHAR(255),
                         location VARCHAR(255),
                         date DATE,
                         time TIME WITH TIME ZONE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "review" (
                          id BIGSERIAL PRIMARY KEY,
                          reviewer_id BIGINT REFERENCES "user"(id),
                          event_id BIGINT REFERENCES event(id),
                          rating INTEGER,
                          comment VARCHAR(500),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "category" (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(50) UNIQUE NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "forum" (
                         id BIGSERIAL PRIMARY KEY,
                         topic VARCHAR(100),
                         created_by BIGINT REFERENCES "user"(id),
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "user_interest" (
                                 id BIGSERIAL PRIMARY KEY,
                                 user_id BIGINT REFERENCES "user"(id),
                                 interest VARCHAR(100),
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "user_event_subscription" (
                                           id BIGSERIAL PRIMARY KEY,
                                           user_id BIGINT REFERENCES "user"(id),
                                           event_id BIGINT REFERENCES event(id),
                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "user_category_subscription" (
                                              id BIGSERIAL PRIMARY KEY,
                                              user_id BIGINT REFERENCES "user"(id),
                                              category_id BIGINT REFERENCES category(id),
                                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "forum_post" (
                              id BIGSERIAL PRIMARY KEY,
                              forum_id BIGINT REFERENCES forum(id),
                              posted_by BIGINT REFERENCES "user"(id),
                              message VARCHAR(500),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE "event" ADD FOREIGN KEY (category_id) REFERENCES "category"(id);