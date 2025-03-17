ALTER TABLE project_member
ADD COLUMN role_id BIGINT;

ALTER TABLE project_member
ADD CONSTRAINT fk_project_member_role
FOREIGN KEY (role_id)
REFERENCES role(role_id);

DROP TABLE user_role;

INSERT INTO role(role_name) VALUES ('Manager');