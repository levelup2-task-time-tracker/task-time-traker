BEGIN;

INSERT INTO public.app_user (user_id, subject, name) VALUES
('a1b2c3d4-e5f6-7890-1234-567890abcdef', '12345', 'Alice Albert');

INSERT INTO public.role (role_name) VALUES
('Developer'),
('Manager');

INSERT INTO public.project (project_id, description, name) VALUES
('11111111-2222-3333-4444-555555555555', 'E-commerce Website Redesign', 'E-commerce Redesign'),
('66666666-7777-8888-9999-aaaaaaaaaaaa', 'Mobile App Feature Development', 'Mobile App Features');

INSERT INTO public.project_member (user_id, project_id, role_id) VALUES
((SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1), (SELECT project_id FROM public.project WHERE name = 'E-commerce Redesign' LIMIT 1), (SELECT role_id FROM public.role WHERE role_name = 'Developer' LIMIT 1)),
((SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1), (SELECT project_id FROM public.project WHERE name = 'E-commerce Redesign' LIMIT 1), (SELECT role_id FROM public.role WHERE role_name = 'Manager' LIMIT 1)),
((SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1), (SELECT project_id FROM public.project WHERE name = 'Mobile App Features' LIMIT 1), (SELECT role_id FROM public.role WHERE role_name = 'Developer' LIMIT 1)),
((SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1), (SELECT project_id FROM public.project WHERE name = 'Mobile App Features' LIMIT 1), (SELECT role_id FROM public.role WHERE role_name = 'Manager' LIMIT 1));

INSERT INTO public.task (task_id, name, description, project_id, story_points) VALUES
('b1b2c3d4-e5f6-7890-1234-567890abcdef', 'Homepage UI Revamp', 'Redesign the homepage user interface.', (SELECT project_id FROM public.project WHERE name = 'E-commerce Redesign' LIMIT 1), 5),
('c1b2c3d4-e5f6-7890-1234-567890abcdef', 'Product Page Optimization', 'Improve the product page loading speed.', (SELECT project_id FROM public.project WHERE name = 'E-commerce Redesign' LIMIT 1), 3),
('d1b2c3d4-e5f6-7890-1234-567890abcdef', 'Shopping Cart Integration', 'Integrate a new shopping cart system.', (SELECT project_id FROM public.project WHERE name = 'E-commerce Redesign' LIMIT 1), 8),
('e1b2c3d4-e5f6-7890-1234-567890abcdef', 'Payment Gateway Setup', 'Configure the payment gateway.', (SELECT project_id FROM public.project WHERE name = 'E-commerce Redesign' LIMIT 1), 5),
('f1b2c3d4-e5f6-7890-1234-567890abcdef', 'User Profile Update', 'Implement user profile update functionality.', (SELECT project_id FROM public.project WHERE name = 'E-commerce Redesign' LIMIT 1), 3),
('a1c2c3d4-e5f6-7890-1234-567890abcdef', 'Search Functionality Enhancement', 'Enhance the website search functionality.', (SELECT project_id FROM public.project WHERE name = 'E-commerce Redesign' LIMIT 1), 8),
('b1c2c3d4-e5f6-7890-1234-567890abcdef', 'Mobile Responsiveness Fixes', 'Fix mobile responsiveness issues.', (SELECT project_id FROM public.project WHERE name = 'E-commerce Redesign' LIMIT 1), 2);

INSERT INTO public.task (task_id, name, description, project_id, story_points) VALUES
('c1c2c3d4-e5f6-7890-1234-567890abcdef', 'Push Notification Implementation', 'Implement push notifications for the app.', (SELECT project_id FROM public.project WHERE name = 'Mobile App Features' LIMIT 1), 5),
('d1c2c3d4-e5f6-7890-1234-567890abcdef', 'Location Services Integration', 'Integrate location services for user location.', (SELECT project_id FROM public.project WHERE name = 'Mobile App Features' LIMIT 1), 3),
('e1c2c3d4-e5f6-7890-1234-567890abcdef', 'Offline Mode Development', 'Develop offline mode functionality.', (SELECT project_id FROM public.project WHERE name = 'Mobile App Features' LIMIT 1), 8),
('f1c2c3d4-e5f6-7890-1234-567890abcdef', 'User Authentication Update', 'Update user authentication system.', (SELECT project_id FROM public.project WHERE name = 'Mobile App Features' LIMIT 1), 5);

INSERT INTO public.time_log (task_id, start_date_time, end_date_time, user_id) VALUES
((SELECT task_id FROM public.task WHERE name = 'Homepage UI Revamp' LIMIT 1), '2025-03-03 09:00:00', '2025-03-03 12:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'Homepage UI Revamp' LIMIT 1), '2025-03-03 13:00:00', '2025-03-03 17:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'Homepage UI Revamp' LIMIT 1), '2025-03-04 09:00:00', '2025-03-04 11:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'Product Page Optimization' LIMIT 1), '2025-03-04 10:00:00', '2025-03-04 16:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'Product Page Optimization' LIMIT 1), '2025-03-05 13:00:00', '2025-03-05 17:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'Shopping Cart Integration' LIMIT 1), '2025-03-06 09:00:00', '2025-03-06 17:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'Shopping Cart Integration' LIMIT 1), '2025-03-07 09:00:00', '2025-03-07 12:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'Shopping Cart Integration' LIMIT 1), '2025-03-07 13:00:00', '2025-03-07 16:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'Payment Gateway Setup' LIMIT 1), '2025-03-08 11:00:00', '2025-03-08 15:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'Payment Gateway Setup' LIMIT 1), '2025-03-10 09:00:00', '2025-03-10 11:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'User Profile Update' LIMIT 1), '2025-03-10 09:00:00', '2025-03-10 16:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'User Profile Update' LIMIT 1), '2025-03-11 13:00:00', '2025-03-11 16:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'Mobile Responsiveness Fixes' LIMIT 1), '2025-03-11 10:00:00', '2025-03-11 12:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'Mobile Responsiveness Fixes' LIMIT 1), '2025-03-12 14:00:00', '2025-03-12 17:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'Push Notification Implementation' LIMIT 1), '2025-03-12 13:00:00', '2025-03-12 17:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'Location Services Integration' LIMIT 1), '2025-03-13 09:00:00', '2025-03-13 16:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'Location Services Integration' LIMIT 1), '2025-03-14 10:00:00', '2025-03-14 12:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1)),
((SELECT task_id FROM public.task WHERE name = 'Offline Mode Development' LIMIT 1), '2025-03-14 10:00:00', '2025-03-14 14:00:00', (SELECT user_id FROM public.app_user WHERE subject = '12345' LIMIT 1));