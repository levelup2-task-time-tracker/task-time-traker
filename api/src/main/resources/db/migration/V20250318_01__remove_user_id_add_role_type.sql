ALTER TABLE task DROP CONSTRAINT IF EXISTS task_user_id_fkey;

ALTER TABLE task DROP COLUMN user_id;

ALTER TABLE task ADD COLUMN role_type INT;

ALTER TABLE task ADD CONSTRAINT task_role_type_id_fkey FOREIGN KEY (role_type) REFERENCES role(role_id);