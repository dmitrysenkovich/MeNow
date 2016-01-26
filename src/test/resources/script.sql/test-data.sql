INSERT INTO users(nick, email, status, login, password, phone_number, notify, access, role, permission_type)
	VALUES('test1', 'test1', 'test1', 'test1', 'test1', 'test1', 0, 1, 'USER', 0);
INSERT INTO users(nick, email, status, login, password, phone_number, notify, access, role, permission_type)
	VALUES('test2', 'test2', 'test2', 'test2', 'test2', 'test2', 0, 1, 'USER', 0);
INSERT INTO users(nick, email, status, login, password, phone_number, notify, access, role, permission_type)
	VALUES('test3', 'test3', 'test3', 'test3', 'test3', 'test3', 0, 1, 'USER', 0);
	
INSERT INTO user_relations(owner_id, viewer_id, is_allowed, is_follower) 
	VALUES(1, 2, 1, 1);
INSERT INTO user_relations(owner_id, viewer_id, is_allowed, is_follower) 
	VALUES(1, 3, 0, 0);
	
INSERT INTO posts(user_id, message, created_date)
	VALUES(2, 'test1', '2015-11-26 00:00:00.0');
INSERT INTO posts(user_id, message, created_date)
	VALUES(2, 'test1', '2015-11-06 00:00:00.0');
INSERT INTO posts(user_id, message, created_date)
	VALUES(2, 'test1', '2015-11-10 00:00:00.0');
INSERT INTO posts(user_id, message, created_date)
	VALUES(3, 'test1', '2015-11-10 00:00:00.0');

INSERT INTO likes(post_id, user_id)
  VALUES(1, 1);
INSERT INTO likes(post_id, user_id)
  VALUES(1, 2);