CREATE OR REPLACE FUNCTION get_project_task_time_in_seconds(project_id_input UUID)
RETURNS TABLE (task_id UUID, task_name VARCHAR, total_seconds NUMERIC) AS $$
BEGIN
    RETURN QUERY
    SELECT
        t.task_id,
        t.name,
        COALESCE(SUM(
            EXTRACT(HOUR FROM (tl.end_date_time - tl.start_date_time)) * 3600 +
            EXTRACT(MINUTE FROM (tl.end_date_time - tl.start_date_time)) * 60 +
            EXTRACT(SECOND FROM (tl.end_date_time - tl.start_date_time))
        ), 0)::NUMERIC AS total_seconds
    FROM
        task t
    JOIN
        time_log tl ON t.task_id = tl.task_id
    WHERE
        t.project_id = project_id_input
    GROUP BY
        t.task_id, t.name
    ORDER BY
        t.name;
END;
$$ LANGUAGE plpgsql;