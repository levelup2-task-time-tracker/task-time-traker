package com.devtools.task_time_tracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProjectModelTest {

    private ProjectModel project;
    private UUID projectId;
    private LocalDateTime deletedAt;

    @BeforeEach
    void setUp() {
        projectId = UUID.randomUUID();
        deletedAt = LocalDateTime.now();
        project = new ProjectModel("Test Project", "Test Description");
        project.setProjectId(projectId);
        project.setDeletedAt(deletedAt);
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
        assertEquals(deletedAt, project.getDeletedAt());
    }
}