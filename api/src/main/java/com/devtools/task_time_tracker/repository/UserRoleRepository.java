package com.devtools.task_time_tracker.repository;

import com.devtools.task_time_tracker.model.UserRoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoleModel, Long> {
}