//package com.devtools.task_time_tracker.ServiceTests;
//
//import com.devtools.task_time_tracker.model.*;
//import com.devtools.task_time_tracker.repository.*;
//import com.devtools.task_time_tracker.service.ProjectService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ProjectServiceTest {
//
//    @Mock
//    private ProjectRepository projectRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private TaskRepository taskRepository;
//
//    @Mock
//    private ProjectMemberRepository projectMemberRepository;
//
//
//    @InjectMocks
//    private ProjectService projectService;
//
//    private UserModel user;
//    private ProjectModel project;
//    private RoleModel role;
//    private ProjectMemberModel projectMember;
//
//    @BeforeEach
//    void setUp() {
////        user = new UserModel();
////        user.setUserId(UUID.randomUUID());
////
////        project = new ProjectModel("Test Project", "Test Description");
////        project.setProjectId(UUID.randomUUID());
//
////        role = new RoleModel();
////        role.setRoleName("Manager");
////
////        projectMember = new ProjectMemberModel(project, user, role);
////
////        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
////        when(projectRepository.findById(project.getProjectId())).thenReturn(Optional.of(project));
////        when(roleRepository.findByRoleName("Manager")).thenReturn(Optional.of(role));
////        when(projectMemberRepository.findByUserAndProject(user, project)).thenReturn(Optional.of(projectMember));
//    }
//
//    @Test
//    void testCreateProject() {
//        when(projectRepository.save(any(ProjectModel.class))).thenReturn(project);
//        when(projectMemberRepository.save(any(ProjectMemberModel.class))).thenReturn(projectMember);
//
//        ProjectModel createdProject = projectService.createProject("Test Project", "Test Description");
//
//        assertNotNull(createdProject);
//        assertEquals("Test Project", createdProject.getName());
//        assertEquals("Test Description", createdProject.getDescription());
//    }
//
//    @Test
//    void testGetUserProjects() {
//        when(projectRepository.findByUser(user.getUserId())).thenReturn(Arrays.asList(project));
//
//        List<ProjectModel> projects = projectService.getUserProjects();
//
//        assertNotNull(projects);
//        assertEquals(1, projects.size());
//        assertEquals("Test Project", projects.get(0).getName());
//    }
//
//    @Test
//    void testUpdateProject() {
//        when(projectRepository.save(any(ProjectModel.class))).thenReturn(project);
//
//        ProjectModel updatedProject = projectService.updateProject(project.getProjectId(), "New Description", "New Name");
//
//        assertNotNull(updatedProject);
//        assertEquals("New Description", updatedProject.getDescription());
//        assertEquals("New Name", updatedProject.getName());
//    }
//
//    @Test
//    void testDeleteProject() {
////        Boolean result = projectService.deleteProject(project.getProjectId());
////
////        assertTrue(result);
////        assertNotNull(project.getDeletedAt());
//    }
//
//    @Test
//    void testRestoreProject() {
////        project.setDeletedAt(LocalDateTime.now());
////
////        Boolean result = projectService.restoreProject(project.getProjectId());
////
////        assertTrue(result);
////        assertNull(project.getDeletedAt());
//    }
//}