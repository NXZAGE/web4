CREATE TABLE
  users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(256) UNIQUE NOT NULL,
    password VARCHAR(256) NOT NULL
  );

INSERT INTO
  users (username, password)
VALUES
  ('admin', 'admin_hash_123'),
  ('guest', 'guest_hash_456');