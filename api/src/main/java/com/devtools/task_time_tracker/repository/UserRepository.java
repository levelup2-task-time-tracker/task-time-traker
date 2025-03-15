package com.devtools.task_time_tracker.repository;

import com.devtools.task_time_tracker.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findBySubject(String subject);
}
