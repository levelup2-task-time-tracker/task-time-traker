package com.devtools.task_time_tracker.repository;

import com.devtools.task_time_tracker.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskModel, Long> {
}
