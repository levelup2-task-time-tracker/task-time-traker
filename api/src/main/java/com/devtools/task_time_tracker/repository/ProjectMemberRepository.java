package com.devtools.task_time_tracker.repository;

import com.devtools.task_time_tracker.model.ProjectMemberModel;
import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.RoleModel;
import com.devtools.task_time_tracker.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ProjectMemberRepository extends JpaRepository<ProjectMemberModel, Long> {
    Optional<ProjectMemberModel> findByUserAndProject(UserModel user, ProjectModel project);
    Optional<ProjectMemberModel> findByUserAndProjectAndRole(UserModel user, ProjectModel project, RoleModel role);

    void deleteByUserAndProject(UserModel userModel, ProjectModel project);
}
