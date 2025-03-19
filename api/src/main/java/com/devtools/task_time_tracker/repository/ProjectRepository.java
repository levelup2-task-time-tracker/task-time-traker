package com.devtools.task_time_tracker.repository;

import com.devtools.task_time_tracker.model.ProjectModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<ProjectModel, UUID> {

    @Query(value = "SELECT p.* FROM project p " +
            "JOIN project_member pm ON p.project_id = pm.project_id " +
            "WHERE pm.user_id = :userId", nativeQuery = true)
    List<ProjectModel> findByUser(@Param("userId") UUID userId);
    Optional<ProjectModel> findByName(String name);
    @Query(value = "SELECT get_total_project_time_in_seconds(:projectId)", nativeQuery = true)
    long getTotalProjectTimeInSeconds(@Param("projectId") UUID projectId);

    @Query(value = "SELECT get_project_member_time_in_seconds(:projectId)", nativeQuery = true)
    List<Object[]> getProjectMembersTimeInSeconds(@Param("projectId") UUID projectId);

    @Query(value = "SELECT get_project_task_time_in_seconds(:projectId)", nativeQuery = true)
    List<Object[]> getProjectTasksTimeInSeconds(@Param("projectId") UUID projectId);

    @Query(value = "SELECT get_project_total_story_points(:projectId, :completedOnly)", nativeQuery = true)
    int getProjectTotalStoryPoints(@Param("projectId") UUID projectId, @Param("completedOnly") boolean completedOnly);

    @Query(value = "SELECT get_project_avg_seconds_per_completed_story_point(:projectId)", nativeQuery = true)
    double getProjectAverageTimeInSecondsPerCompletedStoryPoints(@Param("projectId") UUID projectId);
}