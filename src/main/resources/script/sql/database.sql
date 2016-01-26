  CREATE TABLE IF NOT EXISTS users
  (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    nick VARCHAR(16),
    email VARCHAR(32),
    status VARCHAR(80),
    login VARCHAR(32),
    password VARCHAR(64),
    phone_number VARCHAR(16),
    notify TINYINT DEFAULT 0,
    access TINYINT DEFAULT 1,
    role VARCHAR(8) DEFAULT "USER",
    permission_type TINYINT UNSIGNED DEFAULT 0,
    avatar_image_name VARCHAR(64),
    avatar_image LONGBLOB,
    PRIMARY KEY(id),
    UNIQUE(login)
  );

  CREATE TABLE IF NOT EXISTS user_relations
  (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    owner_id INT UNSIGNED NOT NULL,
    viewer_id INT UNSIGNED NOT NULL,
    is_allowed TINYINT DEFAULT 1,
    is_follower TINYINT DEFAULT 0,
    PRIMARY KEY(id),
	  UNIQUE(owner_id, viewer_id),
    FOREIGN KEY(owner_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(viewer_id) REFERENCES users(id) ON DELETE CASCADE
  );

  CREATE TABLE IF NOT EXISTS posts
  (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id INT UNSIGNED NOT NULL,
    message VARCHAR(80),
	  created_date TIMESTAMP NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
  );
  
  CREATE TABLE IF NOT EXISTS likes
  (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
	  post_id INT UNSIGNED NOT NULL,
    user_id INT UNSIGNED NOT NULL,
    PRIMARY KEY(id),
    UNIQUE(post_id, user_id),
	  FOREIGN KEY(post_id) REFERENCES posts(id) ON DELETE CASCADE
  );