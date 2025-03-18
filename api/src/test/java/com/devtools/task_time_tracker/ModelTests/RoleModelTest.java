package com.devtools.task_time_tracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RoleModelTest {

    private RoleModel role;

    @BeforeEach
    void setUp() {
        role = new RoleModel();
        role.setRoleId(1L);
        role.setRoleName("Manager");
    }

    @Test
    void testConstructor() {
        RoleModel newRole = new RoleModel();
        assertNotNull(newRole);
    }

    @Test
    void testSettersAndGetters() {
        role.setRoleId(2L);
        role.setRoleName("Developer");

        assertEquals(2L, role.getRoleId());
        assertEquals("Developer", role.getRoleName());
    }
}