package com.devtools.task_time_tracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskModelTest {

    private TaskModel task;
    private UUID taskId;
    private LocalDateTime deletedAt;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        deletedAt = LocalDateTime.now();
        task = new TaskModel();
        task.setTaskId(taskId);
        task.setName("Test Task");
        task.setDescription("Test Description");
        task.setStoryPoints(5L);
        task.setDeletedAt(deletedAt);
    }

    @Test
    void testSettersAndGetters() {
        ProjectModel project = new ProjectModel("Test Project", "Test Description");
        UserModel user = new UserModel(1L, "John Doe", "john.doe@example.com");

        task.setProject(project);
        task.setUser(user);

        assertEquals(taskId, task.getTaskId());
        assertEquals("Test Task", task.getName());
        assertEquals("Test Description", task.getDescription());
        assertEquals(5L, task.getStoryPoints());
        assertEquals(deletedAt, task.getDeletedAt());
        assertEquals(project, task.getProject());
        assertEquals(user, task.getUser());
    }
}