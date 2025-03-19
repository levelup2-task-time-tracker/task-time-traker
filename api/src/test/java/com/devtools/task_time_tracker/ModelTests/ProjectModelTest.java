package com.devtools.task_time_tracker.ModelTests;

import com.devtools.task_time_tracker.model.ProjectModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProjectModelTest {

    private ProjectModel project;
    private UUID projectId;
    private LocalDateTime completedAt;

    @BeforeEach
    void setUp() {
        projectId = UUID.randomUUID();
        completedAt = LocalDateTime.now();
        project = new ProjectModel("Test Project", "Test Description");
//        project.setProjectId(projectId);
        project.setCompletedAt(completedAt);
    }

    @Test
    void testConstructor() {
        ProjectModel newProject = new ProjectModel("New Project", "New Description");
        assertNotNull(newProject);
        assertEquals("New Project", newProject.getName());
        assertEquals("New Description", newProject.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        project.setName("Updated Project");
        project.setDescription("Updated Description");

        assertEquals("Updated Project", project.getName());
        assertEquals("Updated Description", project.getDescription());
        assertEquals(projectId, project.getProjectId());
//        assertEquals(completedAt, project.getCompletedAt());
    }
}