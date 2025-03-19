CREATE OR REPLACE FUNCTION sync_deleted_at_update()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.completed_at IS DISTINCT FROM NEW.completed_at THEN
        UPDATE task
        SET completed_at = NEW.completed_at
        WHERE project_id = NEW.project_id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER cascade_completed_at_update
AFTER UPDATE ON project
FOR EACH ROW
EXECUTE FUNCTION sync_completed_at_update();
