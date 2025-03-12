package com.devtools.task_time_tracker.repository;

import com.devtools.task_time_tracker.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {
}
