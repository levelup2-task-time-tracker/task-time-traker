package com.devtools.task_time_tracker.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.RoleModel;
import com.devtools.task_time_tracker.model.UserModel;
import com.devtools.task_time_tracker.repository.ProjectMemberRepository;
import com.devtools.task_time_tracker.repository.ProjectRepository;
import com.devtools.task_time_tracker.repository.RoleRepository;
import com.devtools.task_time_tracker.repository.TaskRepository;
import com.devtools.task_time_tracker.repository.UserRepository;
import com.devtools.task_time_tracker.service.ProjectService;
import com.devtools.task_time_tracker.utils.SharedFunctions;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private SharedFunctions sharedFunctions;

    @Mock
    private UserModel mockUser;

    @Mock
    private ProjectModel mockProject;

    @Mock
    private RoleModel mockRole;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new UserModel();
        mockUser.setUserId(UUID.randomUUID());

        mockProject = new ProjectModel("Test Project", "Test Description");
        UUID projectId = UUID.randomUUID();

        mockRole = new RoleModel();
        mockRole.setRoleName("Manager");

        when(sharedFunctions.findProject(projectId)).thenReturn(mockProject);
        when(sharedFunctions.getLoggedInUser()).thenReturn(mockUser);
    }
  

    @Test
    void testUpdateProjectNotManager() {
        UUID projectId = mockProject.getProjectId();
        String newDescription = "Updated Description";

        when(userRepository.findBySubject(anyString())).thenReturn(Optional.of(mockUser));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(mockProject));
        when(roleRepository.findByRoleName("Manager")).thenReturn(Optional.of(mockRole));
        when(projectMemberRepository.findByUserAndProjectAndRole(mockUser, mockProject, mockRole)).thenReturn(Optional.empty()); 

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                projectService.updateProject(projectId, newDescription, null)
        );
        assertEquals("401 UNAUTHORIZED \"You are not the manager of the project\"", exception.getMessage());
    }


    @Test
    void testDeleteProjectNotManager() {
        UUID projectId = mockProject.getProjectId();

        when(userRepository.findBySubject(anyString())).thenReturn(Optional.of(mockUser));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(mockProject));
        when(roleRepository.findByRoleName("Manager")).thenReturn(Optional.of(mockRole));
        when(projectMemberRepository.findByUserAndProjectAndRole(mockUser, mockProject, mockRole)).thenReturn(Optional.empty()); 

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                projectService.deleteProject(projectId)
        );
        assertEquals("401 UNAUTHORIZED \"You are not the manager of the project\"", exception.getMessage());
    }
 
}
