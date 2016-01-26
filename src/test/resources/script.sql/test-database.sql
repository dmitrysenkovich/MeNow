  CREATE SCHEMA sa;

  CREATE TABLE users
  (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    nick VARCHAR(64),
    email VARCHAR(64),
    status VARCHAR(4096),
    login VARCHAR(32),
    password VARCHAR(64),
    phone_number VARCHAR(16),
    notify INT DEFAULT 0,
    access INT DEFAULT 1,
    role VARCHAR(16) DEFAULT 'USER',
    permission_type INT DEFAULT 0,
    avatar_image_name VARCHAR(64),
    avatar_image BLOB,
    PRIMARY KEY(id),
    UNIQUE(login)
  );

  CREATE TABLE user_relations
  (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    owner_id INT NOT NULL,
    viewer_id INT NOT NULL,
    is_allowed INT DEFAULT 1,
    is_follower INT DEFAULT 0,
    PRIMARY KEY(id),
    FOREIGN KEY(owner_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY(viewer_id) REFERENCES Users(id) ON DELETE CASCADE
  );

  CREATE TABLE posts
  (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id INT NOT NULL,
    message VARCHAR(4096),
	  created_date TIMESTAMP NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(user_id) REFERENCES Users(id) ON DELETE CASCADE
  );

  CREATE TABLE likes
  (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	  post_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY(id),
	  FOREIGN KEY(post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
  );