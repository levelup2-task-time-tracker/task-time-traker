ALTER TABLE project
ADD COLUMN completed_at TIMESTAMP DEFAULT NULL;

ALTER TABLE task
ADD COLUMN completed_at TIMESTAMP DEFAULT NULL;
