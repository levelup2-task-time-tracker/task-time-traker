CREATE OR REPLACE FUNCTION get_project_member_time_in_seconds(project_id_input UUID)
RETURNS TABLE (user_id UUID, user_name VARCHAR, total_seconds NUMERIC) AS $$
BEGIN
    RETURN QUERY
    SELECT
        au.user_id,
        au.name,
        COALESCE(SUM(
            EXTRACT(HOUR FROM (tl.end_date_time - tl.start_date_time)) * 3600 +
            EXTRACT(MINUTE FROM (tl.end_date_time - tl.start_date_time)) * 60 +
            EXTRACT(SECOND FROM (tl.end_date_time - tl.start_date_time))
        ), 0)::NUMERIC AS total_seconds
    FROM
        app_user au
    JOIN
        time_log tl ON au.user_id = tl.user_id
    JOIN
        task t ON tl.task_id = t.task_id
    WHERE
        t.project_id = project_id_input
    GROUP BY
        au.user_id, au.name
    ORDER BY
        au.name;
END;
$$ LANGUAGE plpgsql;