ALTER TABLE IF EXISTS time_log
ADD COLUMN IF NOT EXISTS user_id BIGINT;

ALTER TABLE IF EXISTS time_log
ADD CONSTRAINT fk_time_log_user FOREIGN KEY (user_id) REFERENCES app_user(user_id);