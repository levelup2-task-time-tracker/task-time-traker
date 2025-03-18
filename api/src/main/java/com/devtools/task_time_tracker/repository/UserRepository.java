package com.devtools.task_time_tracker.repository;

import com.devtools.task_time_tracker.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    Optional<UserModel> findBySubject(String subject);
    @Query(value = "SELECT u.* FROM app_user u " +
            "JOIN project_member pm ON u.user_id = pm.user_id " +
            "JOIN role r ON pm.role_id = r.role_id " +
            "WHERE pm.project_id = :projectId " +
            "AND (:roles IS NULL OR r.role_name = ANY(string_to_array(:roles, ',')))", nativeQuery = true)
    List<UserModel> findByUserRoleFilter(@Param("projectId") UUID projectId, @Param("roles") String roles);
}
