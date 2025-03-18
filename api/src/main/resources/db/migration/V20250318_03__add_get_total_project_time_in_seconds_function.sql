CREATE OR REPLACE FUNCTION get_total_project_time_in_seconds(project_id_input UUID)
RETURNS NUMERIC AS $$
DECLARE
    total_interval INTERVAL;
    total_seconds NUMERIC;
BEGIN
    SELECT COALESCE(SUM(tl.end_date_time - tl.start_date_time), '0 seconds')
    INTO total_interval
    FROM task t
    JOIN time_log tl ON t.task_id = tl.task_id
    WHERE t.project_id = project_id_input;

    total_seconds := EXTRACT(HOUR FROM total_interval) * 3600 +
                     EXTRACT(MINUTE FROM total_interval) * 60 +
                     EXTRACT(SECOND FROM total_interval);

    RETURN total_seconds;
END;
$$ LANGUAGE plpgsql;