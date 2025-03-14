package com.devtools.task_time_tracker.repository;

import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectModel, Long> {
    List<ProjectModel> findByManager(UserModel userId);
}