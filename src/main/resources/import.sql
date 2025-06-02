-- USERS
INSERT INTO users (user_id, email, display_name, bio, location, created_at, password, profile_name, role, skills, interests) VALUES (1, 'a@gmail.com', 'Andreas', 'I am Andreas', 'Cyprus', CURRENT_TIMESTAMP, '$2a$10$T2uH.1PVEuAfLrqP9LVAl.j.GP8Ty52KEV99dXQuB4iGNaMtR3.GS', 'andreas', 'USER', 'Java, Spring', 'Reading, Coding');

INSERT INTO users (user_id, email, display_name, bio, location, created_at, password, profile_name, role, skills, interests) VALUES (2, 't@gmail.com', 'Thanos', 'I am Thanos', 'Thessaloniki', CURRENT_TIMESTAMP, '$2a$10$T2uH.1PVEuAfLrqP9LVAl.j.GP8Ty52KEV99dXQuB4iGNaMtR3.GS', 'thanos', 'USER', 'Python, Docker', 'Gaming, Music');

-- USER LINKS
INSERT INTO user_link (id, name, url, user_id) VALUES (1, 'GitHub', 'https://github.com/user1', 1);

INSERT INTO user_link (id, name, url, user_id) VALUES (2, 'LinkedIn', 'https://linkedin.com/in/user1', 1);

INSERT INTO user_link (id, name, url, user_id) VALUES (3, 'GitHub', 'https://github.com/user2', 2);

INSERT INTO user_link (id, name, url, user_id) VALUES (4, 'LinkedIn', 'https://linkedin.com/in/user2', 2);

-- === IMPORTANT: Reset sequences after manual inserts ===
-- Reset sequence for users table
SELECT setval('users_user_id_seq', (SELECT MAX(user_id) FROM users), true);
-- Reset sequence for user_link table
SELECT setval('user_link_id_seq', (SELECT MAX(id) FROM user_link), true);