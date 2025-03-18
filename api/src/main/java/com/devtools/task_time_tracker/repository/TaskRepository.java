package com.devtools.task_time_tracker.repository;

import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.TaskModel;
import com.devtools.task_time_tracker.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskModel, UUID> {
    List<TaskModel> findByProject(ProjectModel project);
}
