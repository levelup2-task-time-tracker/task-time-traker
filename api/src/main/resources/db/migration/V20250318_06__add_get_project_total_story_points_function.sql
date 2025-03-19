CREATE OR REPLACE FUNCTION get_project_total_story_points(project_id_input UUID, completed_only BOOLEAN DEFAULT FALSE)
RETURNS INTEGER AS $$
DECLARE
    total_story_points INTEGER;
BEGIN
    IF completed_only THEN
        SELECT COALESCE(SUM(story_points), 0)
        INTO total_story_points
        FROM task
        WHERE project_id = project_id_input
          AND completed_at IS NOT NULL;
    ELSE
        SELECT COALESCE(SUM(story_points), 0)
        INTO total_story_points
        FROM task
        WHERE project_id = project_id_input;
    END IF;

    RETURN total_story_points;

END;
$$ LANGUAGE plpgsql;