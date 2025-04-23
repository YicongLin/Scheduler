CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    created_at DATE NOT NULL
);

CREATE TABLE profiles (
    id SERIAL PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    bio TEXT,
    avatar_url TEXT NOT NULL,
    user_id INTEGER UNIQUE NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE
);
