package com.devtools.task_time_tracker.repository;

import com.devtools.task_time_tracker.model.UserModel;
import com.devtools.task_time_tracker.model.UserRoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleModel, Long> {
    List<UserRoleModel> findByUser(UserModel user);
}