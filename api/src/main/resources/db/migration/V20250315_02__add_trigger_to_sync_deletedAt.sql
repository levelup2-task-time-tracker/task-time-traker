CREATE OR REPLACE FUNCTION sync_deleted_at_update()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.deleted_at IS DISTINCT FROM NEW.deleted_at THEN
        UPDATE task
        SET deleted_at = NEW.deleted_at
        WHERE project_id = NEW.project_id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER cascade_deleted_at_update
AFTER UPDATE ON project
FOR EACH ROW
EXECUTE FUNCTION sync_deleted_at_update();
