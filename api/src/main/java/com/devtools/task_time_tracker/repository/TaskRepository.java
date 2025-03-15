package com.devtools.task_time_tracker.repository;

import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskModel, Long> {
    List<TaskModel> findByProject(ProjectModel project);
}
