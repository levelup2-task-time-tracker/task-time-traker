CREATE DATABASE task_time_tracker;

CREATE TABLE app_user (
    user_id SERIAL PRIMARY KEY,
    subject VARCHAR(30)
);


CREATE TABLE project (
    project_id SERIAL PRIMARY KEY,
    description VARCHAR(255),
    manager BIGINT,
    FOREIGN KEY (manager) REFERENCES app_user(user_id)
);

CREATE TABLE task (
    task_id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    user_id BIGINT,
    project_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES app_user(user_id),
    FOREIGN KEY (project_id) REFERENCES project(project_id)
);

CREATE TABLE time_log (
    time_log_id SERIAL PRIMARY KEY,
    task_id BIGINT,
    start_date_time TIMESTAMP,
    end_date_time TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES task(task_id)
);

CREATE TABLE role (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(255)
);

CREATE TABLE user_role (
    user_role_id SERIAL PRIMARY KEY,
    user_id BIGINT,
    role_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES app_user(user_id),
    FOREIGN KEY (role_id) REFERENCES role(role_id)
);