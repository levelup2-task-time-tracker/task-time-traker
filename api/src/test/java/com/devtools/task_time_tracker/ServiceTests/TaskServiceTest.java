//package com.devtools.task_time_tracker.ServiceTests;
//
//import com.devtools.task_time_tracker.model.TaskModel;
//import com.devtools.task_time_tracker.service.TaskService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class TaskServiceTest {
//
//    @InjectMocks
//    private TaskService taskService;
//
//    private UUID projectId;
//    private UUID taskId;
//
//    @BeforeEach
//    void setUp() {
//        projectId = UUID.randomUUID();
//        taskId = UUID.randomUUID();
//    }
//
//    @Test
//    void testCreateTask() {
//        TaskModel task = taskService.createTask("Test Description", "Test Name", 5L, projectId);
//
//        assertNotNull(task);
//        // Add more assertions based on the actual implementation of TaskModel
//    }
//
//    @Test
//    void testUpdateTask() {
//        TaskModel task = taskService.updateTask(taskId, "New Description", "New Name", 8L);
//
//        assertNotNull(task);
//        // Add more assertions based on the actual implementation of TaskModel
//    }
//
//    @Test
//    void testDeleteTask() {
//        Boolean result = taskService.deleteTask(taskId);
//
//        assertTrue(result);
//    }
//}