package com.devtools.task_time_tracker.service;

import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.TaskModel;
import com.devtools.task_time_tracker.model.UserModel;
import com.devtools.task_time_tracker.repository.ProjectMemberRepository;
import com.devtools.task_time_tracker.repository.ProjectRepository;
import com.devtools.task_time_tracker.repository.TaskRepository;
import com.devtools.task_time_tracker.repository.UserRepository;
import com.devtools.task_time_tracker.utils.SharedFunctions;
import com.devtools.task_time_tracker.workload_balancer.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpClient;
import java.util.Optional;
import java.util.UUID;

import static com.devtools.task_time_tracker.utils.SharedFunctions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private SharedFunctions sharedFunctions;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask() {
        UUID projectId = UUID.randomUUID();
        String description = "Test Task";
        String name = "Task Name";
        Long storyPoints = 5L;

        ProjectModel mockProject = new ProjectModel();
        UserModel mockUser = new UserModel();
        TaskModel expectedTask = new TaskModel(description, name, storyPoints, mockProject);

        when(sharedFunctions.findProject(projectId)).thenReturn(mockProject);
        when(sharedFunctions.getLoggedInUser()).thenReturn(mockUser);
        doNothing().when(sharedFunctions).verifyUser(mockUser, mockProject);
        when(taskRepository.save(any(TaskModel.class))).thenReturn(expectedTask);

        TaskModel result = taskService.createTask(description, name, storyPoints, projectId);

        assertNotNull(result);
        assertEquals(description, result.getDescription());
        assertEquals(name, result.getName());
        assertEquals(storyPoints, result.getStoryPoints());
        assertEquals(mockProject, result.getProject());

        verify(taskRepository, times(1)).save(any(TaskModel.class));


    }

    @Test
    void updateTaskThrowError() {
        UUID taskId = UUID.randomUUID();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            taskService.updateTask(taskId, "New description", "New name", 9L);
        });

        assertEquals("Task not found", exception.getReason());
    }

    @Test
    void updateTask() {
        UUID taskId = UUID.randomUUID();
        String description = "Test Task";
        String name = "Task Name";
        Long storyPoints = 5L;

        ProjectModel mockProject = new ProjectModel();
        UserModel mockUser = new UserModel();
        TaskModel task = new TaskModel("Old Task", "Old Task Name", 3L, mockProject);
        TaskModel expectedTask = new TaskModel(description, name, storyPoints, mockProject);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(sharedFunctions.findProject(task.getProject().getProjectId())).thenReturn(mockProject);
        when(sharedFunctions.getLoggedInUser()).thenReturn(mockUser);
        doNothing().when(sharedFunctions).verifyUser(mockUser, mockProject);
        when(taskRepository.save(any(TaskModel.class))).thenReturn(expectedTask);

        TaskModel result = taskService.updateTask(taskId, description, name, storyPoints);

        assertNotNull(result);
        assertEquals(description, result.getDescription());
        assertEquals(name, result.getName());
        assertEquals(storyPoints, result.getStoryPoints());

        verify(taskRepository, times(1)).save(any(TaskModel.class));
    }

    @Test
    void deleteTaskThrowError() {
        UUID taskId = UUID.randomUUID();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            taskService.deleteTask(taskId);
        });

        assertEquals("Task not found", exception.getReason());
    }


    @Test
    void deleteTask() {
        UUID taskId = UUID.randomUUID();
        ProjectModel mockProject = new ProjectModel();
        UserModel mockUser = new UserModel();
        TaskModel task = new TaskModel("Old Task", "Old Task Name", 3L, mockProject);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(sharedFunctions.findProject(task.getProject().getProjectId())).thenReturn(mockProject);
        when(sharedFunctions.getLoggedInUser()).thenReturn(mockUser);
        doNothing().when(sharedFunctions).verifyUser(mockUser, mockProject);
        boolean result = taskService.deleteTask(taskId);

        assertTrue(result);
    }
}