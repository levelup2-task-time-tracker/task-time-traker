package com.devtools.task_time_tracker.repository;

import com.devtools.task_time_tracker.model.ProjectMemberModel;
import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.UserModel;
import com.devtools.task_time_tracker.workload_balancer.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


public interface ProjectMemberRepository extends JpaRepository<ProjectMemberModel, Long> {
    Optional<ProjectMemberModel> findByUserAndProject(UserModel user, ProjectModel project);

    List<ProjectMemberModel> findByUser(UserModel user);
    @Query(value = "SELECT au.user_id, au.name, " +
            "COALESCE(SUM(EXTRACT(EPOCH FROM (tl.end_date_time - tl.start_date_time)) / 3600), 0) AS workLoad " +
            "FROM project_member pm " +
            "LEFT JOIN time_log tl ON pm.user_id = tl.user_id " +
            "JOIN app_user au ON au.user_id = pm.user_id " +
            "WHERE pm.project_id = :projectId " +
            "GROUP BY pm.user_id, au.name, au.user_id",
            nativeQuery = true)
    List<Object> sumByUser(@Param("projectId") UUID projectId);


}
