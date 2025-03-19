package com.devtools.task_time_tracker.repository;

import com.devtools.task_time_tracker.model.ProjectMemberModel;
import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.RoleModel;
import com.devtools.task_time_tracker.model.UserModel;
import com.devtools.task_time_tracker.workload_balancer.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


public interface ProjectMemberRepository extends JpaRepository<ProjectMemberModel, Long> {
    Optional<ProjectMemberModel> findByUserAndProject(UserModel user, ProjectModel project);
    Optional<ProjectMemberModel> findByUserAndProjectAndRole(UserModel user, ProjectModel project, RoleModel role);
    List<ProjectMemberModel> findByUserAndRole(UserModel user, RoleModel role);
    List<ProjectMemberModel> findByProject(ProjectModel project);
    List<ProjectMemberModel> findByUser(UserModel user);
    @Query(value = "SELECT au.user_id, au.name, " +
            "COALESCE(SUM(EXTRACT(EPOCH FROM (tl.end_date_time - tl.start_date_time)) / 3600), 0) AS workLoad " +
            "FROM project_member pm " +
            "JOIN role r ON r.role_id = pm.role_id "+
            "LEFT JOIN time_log tl ON pm.user_id = tl.user_id " +
            "JOIN app_user au ON au.user_id = pm.user_id " +
            "WHERE pm.project_id = :projectId AND r.role_name = :role " +
            "GROUP BY pm.user_id, au.name, au.user_id",
            nativeQuery = true)
    List<Object> sumByUser(@Param("projectId") UUID projectId, @Param("role") String role);

    @Query(value = "SELECT t.task_id, t.name, " +
            "COALESCE(t.story_points - SUM(EXTRACT(EPOCH FROM (tl.end_date_time - tl.start_date_time)) / 3600), t.story_points) AS estimatedTime " +
            "FROM task t " +
            "JOIN role r ON r.role_id = t.role_type "+
            "LEFT JOIN time_log tl ON t.task_id = tl.task_id " +
            "JOIN project pm ON t.project_id = pm.project_id " +
            "WHERE pm.project_id = :projectId " +
            "GROUP BY t.task_id, t.name, t.story_points, tl.task_id " +
            "HAVING tl.task_id IS NULL OR COALESCE(t.story_points - SUM(EXTRACT(EPOCH FROM (tl.end_date_time - tl.start_date_time)) / 3600), t.story_points) <> 0",
            nativeQuery = true)
    List<Object> sumByTask(@Param("projectId") UUID projectId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM project_member WHERE user_id = :userId AND project_id = :projectId", nativeQuery = true)
    void deleteByUserAndProject(@Param("userId") UUID userId, @Param("projectId") UUID projectId);



}
