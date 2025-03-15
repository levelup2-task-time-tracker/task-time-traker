package com.devtools.task_time_tracker_cli.command;

import com.devtools.task_time_tracker_cli.component.AuthToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersCommandTest {

    @Mock
    private AuthToken authToken;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UsersCommand usersCommand;

    private static final String BASE_URL = "http://localhost:8080";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usersCommand = new UsersCommand(authToken);
    }

    @Test
    void getUsers_NotAuthenticated_ReturnsErrorMessage() {
        when(authToken.isAuthenticated()).thenReturn(false);

        String result = usersCommand.getUsers();

        assertEquals("You must login first.", result);
        verifyNoInteractions(restTemplate);
    }
}