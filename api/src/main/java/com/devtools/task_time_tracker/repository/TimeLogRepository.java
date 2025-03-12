package com.devtools.task_time_tracker.repository;

import com.devtools.task_time_tracker.model.TimeLogModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeLogRepository extends JpaRepository<TimeLogModel, Long> {
}
