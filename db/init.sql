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

CREATE TABLE
  user_roles (
    user_id INT REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    role_id INT REFERENCES roles(id) ON DELETE CASCADE NOT NULL,
    CONSTRAINT user_roles_pk PRIMARY KEY (user_id, role_id)
  );

CREATE TABLE
  hits (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE NOT NULL, 
    x REAL NOT NULL,
    y REAL NOT NULL,
    r REAL NOT NULL,
    hit BOOLEAN NOT NULL,
    exec_time BIGINT NOT NULL,
    registered_at TIMESTAMPTZ NOT NULL
  );


INSERT INTO
  users (username, password_hash)
VALUES
  ('admin', 'admin_hash_123'),
  ('guest', 'guest_hash_456');