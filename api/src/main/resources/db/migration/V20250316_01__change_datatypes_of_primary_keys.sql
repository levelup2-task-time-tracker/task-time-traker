CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

TRUNCATE TABLE app_user, project, task, time_log, role, user_role RESTART IDENTITY CASCADE;

DO $$
DECLARE
    r RECORD;
BEGIN
    FOR r IN (SELECT constraint_name, table_name
              FROM information_schema.table_constraints
              WHERE constraint_type = 'FOREIGN KEY'
              AND constraint_schema = 'public'
              AND table_name IN ('project', 'task', 'user_role', 'time_log')) LOOP
        EXECUTE 'ALTER TABLE ' || r.table_name || ' DROP CONSTRAINT IF EXISTS ' || r.constraint_name;
    END LOOP;
END $$;

DO $$
DECLARE
    r RECORD;
BEGIN
    FOR r IN (SELECT constraint_name, table_name
              FROM information_schema.table_constraints
              WHERE constraint_type = 'PRIMARY KEY'
              AND constraint_schema = 'public'
              AND table_name IN ('app_user', 'project', 'task')) LOOP
        EXECUTE 'ALTER TABLE ' || r.table_name || ' DROP CONSTRAINT IF EXISTS ' || r.constraint_name;
    END LOOP;
END $$;

ALTER TABLE app_user ALTER COLUMN user_id DROP DEFAULT;
ALTER TABLE project ALTER COLUMN project_id DROP DEFAULT;
ALTER TABLE task ALTER COLUMN task_id DROP DEFAULT;

ALTER TABLE app_user ALTER COLUMN user_id SET DATA TYPE UUID USING gen_random_uuid();
ALTER TABLE project ALTER COLUMN project_id SET DATA TYPE UUID USING gen_random_uuid();
ALTER TABLE task ALTER COLUMN task_id SET DATA TYPE UUID USING gen_random_uuid();

ALTER TABLE task ALTER COLUMN user_id SET DATA TYPE UUID USING user_id::TEXT::UUID;
ALTER TABLE task ALTER COLUMN project_id SET DATA TYPE UUID USING project_id::TEXT::UUID;
ALTER TABLE time_log ALTER COLUMN task_id SET DATA TYPE UUID USING task_id::TEXT::UUID;
ALTER TABLE user_role ALTER COLUMN user_id SET DATA TYPE UUID USING user_id::TEXT::UUID;

ALTER TABLE app_user ADD PRIMARY KEY (user_id);
ALTER TABLE project ADD PRIMARY KEY (project_id);
ALTER TABLE task ADD PRIMARY KEY (task_id);


ALTER TABLE task ADD CONSTRAINT task_user_fkey FOREIGN KEY (user_id) REFERENCES app_user(user_id) ON DELETE SET NULL;
ALTER TABLE task ADD CONSTRAINT task_project_fkey FOREIGN KEY (project_id) REFERENCES project(project_id) ON DELETE CASCADE;
ALTER TABLE time_log ADD CONSTRAINT time_log_task_fkey FOREIGN KEY (task_id) REFERENCES task(task_id) ON DELETE CASCADE;
ALTER TABLE user_role ADD CONSTRAINT user_role_user_fkey FOREIGN KEY (user_id) REFERENCES app_user(user_id) ON DELETE CASCADE;
ALTER TABLE user_role ADD CONSTRAINT user_role_role_fkey FOREIGN KEY (role_id) REFERENCES role(role_id) ON DELETE CASCADE;
