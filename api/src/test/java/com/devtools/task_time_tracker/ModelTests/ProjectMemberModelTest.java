package com.devtools.task_time_tracker.ModelTests;

import com.devtools.task_time_tracker.model.ProjectMemberModel;
import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.RoleModel;
import com.devtools.task_time_tracker.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProjectMemberModelTest {

    private ProjectModel project;
    private UserModel user;
    private RoleModel role;
    private ProjectMemberModel projectMember;

    @BeforeEach
    void setUp() {
        project = new ProjectModel("Test Project", "Test Description");
        user = new UserModel("John Doe", "john.doe@example.com");
        role = new RoleModel();
        projectMember = new ProjectMemberModel(project, user, role);
    }

    @Test
    void testConstructor() {
        assertNotNull(projectMember);
        assertEquals(project, projectMember.getProject());
        assertEquals(user, projectMember.getUser());
        assertEquals(role, projectMember.getRole());
    }

    @Test
    void testSettersAndGetters() {
        ProjectModel newProject = new ProjectModel("New Project", "New Description");
        UserModel newUser = new UserModel("Jane Smith", "jane.smith@example.com");
        RoleModel newRole = new RoleModel();

        projectMember.setProject(newProject);
        projectMember.setUser(newUser);
        projectMember.setRole(newRole);

        assertEquals(newProject, projectMember.getProject());
        assertEquals(newUser, projectMember.getUser());
        assertEquals(newRole, projectMember.getRole());
    }
}