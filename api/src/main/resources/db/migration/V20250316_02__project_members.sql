CREATE TABLE project_member (
    project_member_id SERIAL PRIMARY KEY,
    user_id UUID,
    project_id UUID,
    FOREIGN KEY (user_id) REFERENCES app_user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES project(project_id) ON DELETE CASCADE
);
