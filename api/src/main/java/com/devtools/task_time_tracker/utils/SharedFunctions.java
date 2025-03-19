package com.devtools.task_time_tracker.utils;

import com.devtools.task_time_tracker.model.*;
import com.devtools.task_time_tracker.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SharedFunctions {

    public static UserModel getLoggedInUser(UserRepository userRepository) throws ResponseStatusException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        if (authentication.getPrincipal() instanceof OAuth2IntrospectionAuthenticatedPrincipal principal) {
            Map<String, Object> attributes = principal.getAttributes();
            String subject = (String) attributes.get("sub");
            if (subject == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Subject not found in token");
            }

            Optional<UserModel> user = userRepository.findBySubject(subject);
            if (user.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            return user.get();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authentication principal");
        }
    }

    public static ProjectModel findProject(UUID projectId, ProjectRepository projectRepository) throws ResponseStatusException{
        Optional<ProjectModel> projectModelOptional = projectRepository.findById(projectId);

        if (projectModelOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        return projectModelOptional.get();
    }

    public static UserModel findUser(UUID userId, UserRepository userRepository) throws ResponseStatusException{
        Optional<UserModel> userModelOptional= userRepository.findById(userId);

        if (userModelOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return userModelOptional.get();
    }

    public static RoleModel findRole(String roleName, RoleRepository roleRepository) throws ResponseStatusException{
        Optional<RoleModel> roleModelOptional = roleRepository.findByRoleName(roleName);
        if (roleModelOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }
        return roleModelOptional.get();
    }

    public static  void verifyUser(UserModel user, ProjectModel project, ProjectMemberRepository projectMemberRepository) throws  ResponseStatusException{
        Optional<ProjectMemberModel> projectMemberModel = projectMemberRepository.findByUserAndProject(user, project);
        if (projectMemberModel.isEmpty()){
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not part of the project.");
        }

    }

    public static UUID getUserTaskUuid(ProjectMemberRepository projectMemberRepository, TaskRepository taskRepository, String taskName, UserModel user) throws RuntimeException{
        return projectMemberRepository.findByUser(user).stream()
                .map(ProjectMemberModel::getProject)
                .flatMap(project -> taskRepository.findByProjectAndName(project, taskName).stream())
                .findFirst()
                .map(TaskModel::getTaskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No task with the given name found for user"));
    }

    public static UUID getProjectUuid(ProjectRepository projectRepository, String projectName) {
        return projectRepository.findByName(projectName)
                .map(ProjectModel::getProjectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found: " + projectName));
    }
}