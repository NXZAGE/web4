CREATE TABLE
  users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(256) UNIQUE NOT NULL,
    password_hash VARCHAR(256) NOT NULL
  );

CREATE TABLE
  roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(256) UNIQUE NOT NULL,
    description VARCHAR(256)
  );

INSERT INTO
  users (username, password_hash)
VALUES
  ('admin', 'admin_hash_123'),
  ('guest', 'guest_hash_456');