package com.devtools.task_time_tracker.utils;

import com.devtools.task_time_tracker.model.*;
import com.devtools.task_time_tracker.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SharedFunctionsTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    SharedFunctions sharedFunctions;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private OAuth2IntrospectionAuthenticatedPrincipal principal;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getLoggedInUserSuccess() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(principal);

        Map<String, Object> attributes = Map.of("sub", "123");
        when(principal.getAttributes()).thenReturn(attributes);

        UserModel expectedUser = new UserModel();
        when(userRepository.findBySubject("123")).thenReturn(Optional.of(expectedUser));


        UserModel result = sharedFunctions.getLoggedInUser();

        assertNotNull(result);
        assertEquals(expectedUser, result);
    }

    @Test
    void getLoggedInUserWhenUserIsNotAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            sharedFunctions.getLoggedInUser();
        });

        assertEquals("User not authenticated", exception.getReason());
    }

    @Test
    void getLoggedInUserWhenSubjectIsNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(principal);

        Map<String, Object> attributes = Map.of();
        when(principal.getAttributes()).thenReturn(attributes);


        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            sharedFunctions.getLoggedInUser();
        });

        assertEquals("Subject not found in token", exception.getReason());
    }

    @Test
    void getLoggedInUserThrowsNotFoundException() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(principal);

        Map<String, Object> attributes = Map.of("sub", "123");
        when(principal.getAttributes()).thenReturn(attributes);

        when(userRepository.findBySubject("123")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            sharedFunctions.getLoggedInUser();
        });
        assertEquals("User not found", exception.getReason());
    }

    @Test
    void getLoggedInUserThrowsUnauthorizedException() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(new Object()); // Not OAuth2IntrospectionAuthenticatedPrincipal

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            sharedFunctions.getLoggedInUser();
        });

        assertEquals("Invalid authentication principal", exception.getReason());
    }

    @Test
    void findProjectSuccess() {
        UUID projectId = UUID.randomUUID();
        ProjectModel expectedProject = new ProjectModel();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(expectedProject));

        ProjectModel result = sharedFunctions.findProject(projectId);

        assertNotNull(result);
        assertEquals(expectedProject, result);
    }

    @Test
    void findProjectThrowsNotFoundException() {

        UUID projectId = UUID.randomUUID();
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            sharedFunctions.findProject(projectId);
        });

        assertEquals("Project not found", exception.getReason());
    }

    @Test
    void findUserSuccess() {
        UUID userId = UUID.randomUUID();
        UserModel expectedUser = new UserModel();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        UserModel result = sharedFunctions.findUser(userId);
        assertNotNull(result);
        assertEquals(expectedUser, result);
    }

    @Test
    void findUserThrowsNotFoundException() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            sharedFunctions.findUser(userId);
        });

        assertEquals("User not found", exception.getReason());
    }
    
    @Test
    void findRoleSuccess() {
        String roleName = "ADMIN";
        RoleModel expectedRole = new RoleModel();
        when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.of(expectedRole));
        
        RoleModel result = sharedFunctions.findRole(roleName);

        assertNotNull(result);
        assertEquals(expectedRole, result);
    }

    @Test
    void findRolehrowsNotFoundException() {
        String roleName = "ADMIN";
        when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            sharedFunctions.findRole(roleName);
        });
        assertEquals("Role not found", exception.getReason());
    }

    @Test
    void verifyUserSuccess() {
        UserModel user = new UserModel();
        ProjectModel project = new ProjectModel();
        when(projectMemberRepository.findByUserAndProject(user, project)).thenReturn(Optional.of(new ProjectMemberModel()));

        assertDoesNotThrow(() -> {
            sharedFunctions.verifyUser(user, project);
        });
    }

    @Test
    void verifyUserThrowsUnauthorizedException() {
        UserModel user = new UserModel();
        ProjectModel project = new ProjectModel();
        when(projectMemberRepository.findByUserAndProject(user, project)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            sharedFunctions.verifyUser(user, project);
        });

        assertEquals("You are not part of the project.", exception.getReason());
    }


    @Test
    void getUserTaskUuidThrowsNotFoundException() {
        String taskName = "Task1";
        UserModel user = new UserModel();
        when(projectMemberRepository.findByUser(user)).thenReturn(List.of());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            sharedFunctions.getUserTaskUuid(taskName, user);
        });
        assertEquals("No task with the given name found for user", exception.getReason());
    }

    @Test
    void getProjectUuidSuccess() {
        String projectName = "Project1";
        ProjectModel project = new ProjectModel();
        project.setProjectId(UUID.randomUUID());
        when(projectRepository.findByName(projectName)).thenReturn(Optional.of(project));

        UUID result = sharedFunctions.getProjectUuid(projectName);

        assertNotNull(result);
        assertEquals(project.getProjectId(), result);
    }

    @Test
    void getProjectUuidThrowsNotFoundException() {
        String projectName = "Project1";
        when(projectRepository.findByName(projectName)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            sharedFunctions.getProjectUuid(projectName);
        });

        assertEquals("Project not found: " + projectName, exception.getReason());
    }

    @Test
    void verifyUserTaskSuccess() {
        UUID taskId = UUID.randomUUID();
        UserModel user = new UserModel();
        TaskModel task = new TaskModel();
        ProjectModel project = new ProjectModel();
        task.setProject(project);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectRepository.findById(project.getProjectId())).thenReturn(Optional.of(project));
        when(projectMemberRepository.findByUserAndProject(user, project)).thenReturn(Optional.of(new ProjectMemberModel()));

        TaskModel result = sharedFunctions.verifyUserTask(taskId, user);

        assertNotNull(result);
        assertEquals(task, result);
    }

    @Test
    void verifyUserTaskThrowsNotFoundException() {
        UUID taskId = UUID.randomUUID();
        UserModel user = new UserModel();
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            sharedFunctions.verifyUserTask(taskId, user);
        });
        assertEquals("Task not found", exception.getReason());
    }

    @Test
    void verifyUserTaskThrowsUnauthorizedException() {
        UUID taskId = UUID.randomUUID();
        UserModel user = new UserModel();
        TaskModel task = new TaskModel();
        ProjectModel project = new ProjectModel();
        task.setProject(project);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectRepository.findById(project.getProjectId())).thenReturn(Optional.of(project));
        when(projectMemberRepository.findByUserAndProject(user, project)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            sharedFunctions.verifyUserTask(taskId, user);
        });

        assertEquals("You are not part of the project.", exception.getReason());
    }
    
}

