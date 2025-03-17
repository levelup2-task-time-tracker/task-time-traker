package com.devtools.task_time_tracker.repository;

import com.devtools.task_time_tracker.model.ProjectModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<ProjectModel, UUID> {

    List<ProjectModel> findByIsPrivateFalse();
    @Query(value = "SELECT p.* FROM project p " +
            "JOIN project_member pm ON p.project_id = pm.project_id " +
            "WHERE pm.user_id = :userId", nativeQuery = true)
    List<ProjectModel> findByUser(@Param("userId") UUID userId);

}