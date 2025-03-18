CREATE OR REPLACE FUNCTION get_project_avg_seconds_per_completed_story_point(project_id_input UUID)
RETURNS NUMERIC AS $$
DECLARE
    avg_seconds_per_point NUMERIC;
BEGIN
    SELECT AVG(total_seconds / story_points::NUMERIC)
    INTO avg_seconds_per_point
    FROM (
        SELECT
            t.task_id,
            t.story_points,
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
            AND t.deleted_at IS NOT NULL
        GROUP BY
            t.task_id, t.story_points
    ) AS task_time_data
    WHERE story_points > 0; -- Avoid division by zero

    RETURN COALESCE(avg_seconds_per_point, 0); -- Return 0 if no data
END;
$$ LANGUAGE plpgsql;