package com.devtools.task_time_tracker.utils;

import com.devtools.task_time_tracker.model.ProjectMemberModel;
import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.UserModel;
import com.devtools.task_time_tracker.repository.ProjectRepository;
import com.devtools.task_time_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

}