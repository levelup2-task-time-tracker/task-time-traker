INSERT INTO app_user (subject) VALUES
('1111111111');

INSERT INTO role (role_name) VALUES
('Developer'),
('Tech Lead'),
('QA'),
('BA');

INSERT INTO user_role (user_id, role_id) VALUES
(2, 1);

INSERT INTO project (description, manager) VALUES
('E-commerce Website Redesign', 1),
('Mobile App Feature Development', 1);

INSERT INTO task (name, description, user_id, project_id, story_points) VALUES
('Homepage UI Revamp', 'Redesign the homepage user interface.', 2, 1, 1),
('Product Page Optimization', 'Improve the product page loading speed.', 2, 1, 3),
('Shopping Cart Integration', 'Integrate a new shopping cart system.', 2, 1, 1),
('Payment Gateway Setup', 'Configure the payment gateway.', 2, 1, 2),
('User Profile Update', 'Implement user profile update functionality.', 2, 1, 3),
('Search Functionality Enhancement', 'Enhance the website search functionality.', 2, 1, 1),
('Mobile Responsiveness Fixes', 'Fix mobile responsiveness issues.', 2, 1, 2),
('Push Notification Implementation', 'Implement push notifications for the app.', 2, 2, 2),
('Location Services Integration', 'Integrate location services for user location.', 2, 2, 3),
('Offline Mode Development', 'Develop offline mode functionality.', 2, 2, 3),
('User Authentication Update', 'Update user authentication system.', 2, 2, 3);

INSERT INTO time_log (task_id, start_date_time, end_date_time) VALUES
(1, '2025-03-03 09:00:00', '2025-03-03 12:00:00'),
(1, '2025-03-03 13:00:00', '2025-03-03 17:00:00'),
(1, '2025-03-04 09:00:00', '2025-03-04 11:00:00'),
(2, '2025-03-04 10:00:00', '2025-03-04 16:00:00'),
(2, '2025-03-05 13:00:00', '2025-03-05 17:00:00'),
(3, '2025-03-06 09:00:00', '2025-03-06 17:00:00'),
(3, '2025-03-07 09:00:00', '2025-03-07 12:00:00'),
(3, '2025-03-07 13:00:00', '2025-03-07 16:00:00'),
(4, '2025-03-08 11:00:00', '2025-03-08 15:00:00'),
(4, '2025-03-10 09:00:00', '2025-03-10 11:00:00'),
(5, '2025-03-10 09:00:00', '2025-03-10 16:00:00'),
(5, '2025-03-11 13:00:00', '2025-03-11 16:00:00'),
(7, '2025-03-11 10:00:00', '2025-03-11 12:00:00'),
(7, '2025-03-12 14:00:00', '2025-03-12 17:00:00'),
(8, '2025-03-12 13:00:00', '2025-03-12 17:00:00'),
(9, '2025-03-13 09:00:00', '2025-03-13 16:00:00'),
(9, '2025-03-14 10:00:00', '2025-03-14 12:00:00'),
(10, '2025-03-14 10:00:00', '2025-03-14 14:00:00');
