package com.devtools.task_time_tracker.ModelTests;

import com.devtools.task_time_tracker.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserModelTest {

    private UserModel user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    void testConstructor() {
        UserModel newUser = new UserModel("Jane Smith", "subject456");
        assertNotNull(newUser);
        assertEquals("Jane Smith", newUser.getName());
        assertEquals("subject456", newUser.getSubject());
    }

    @Test
    void testSettersAndGetters() {
        user.setName("Updated Name");
        user.setSubject("updatedSubject");

        assertEquals(userId, user.getUserId());
        assertEquals("Updated Name", user.getName());
    }
}