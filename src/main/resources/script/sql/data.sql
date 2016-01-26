INSERT INTO users(login, password, role, access)
SELECT 'admin', 'admin', 'ADMIN', 1
FROM DUAL
WHERE NOT EXISTS (SELECT *
                  FROM users
                  WHERE login = 'admin');