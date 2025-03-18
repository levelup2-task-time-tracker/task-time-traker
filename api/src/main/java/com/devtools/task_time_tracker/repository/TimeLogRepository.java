package com.devtools.task_time_tracker.repository;

import com.devtools.task_time_tracker.model.TaskModel;
import com.devtools.task_time_tracker.model.TimeLogModel;
import com.devtools.task_time_tracker.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TimeLogRepository extends JpaRepository<TimeLogModel, UUID> {
    List<TimeLogModel> findByTaskAndEndDateTimeIsNull(TaskModel task);
    List<TimeLogModel> findByUserAndTaskAndEndDateTimeIsNull(UserModel user, TaskModel task);
    List<TimeLogModel> findByTask(TaskModel task);
    List<TimeLogModel> findByUser(UserModel user);
}
