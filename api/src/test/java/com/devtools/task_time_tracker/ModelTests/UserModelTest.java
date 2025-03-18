package com.devtools.task_time_tracker.model;

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
        user =Id);
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
       `

### Explanation
- **Annotations:**
  - `@BeforeEach`: Sets up the test environment before each test method.
  - `@Test`: Identifies a method as a test method.

- **Test Methods:**
  - `testConstructor()`: Tests the constructor of `UserModel`.
  - `testSettersAndGetters()`: Tests the setters and getters of `UserModel`.

### Running the Test
You can run your tests using your IDE or by running the following command in your terminal:
```sh
mvn test