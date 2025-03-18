package com.devtools.task_time_tracker.service;
import com.devtools.task_time_tracker.model.UserModel;
import com.devtools.task_time_tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import.when;
   
    @ExtendWith(MockitoExtension.class)
        public class UserServiceTest {
    
    @Mock
        private UserRepository userRepository;
    
    @InjectMocks
        private UserService userService;
        private UserModel user1;
        private UserModel user2;
    
    @BeforeEach
        void setUp() {
        user1 = new UserModel(1L, "John Doe", "john.doe@example.com");
        user2 = new UserModel(2L, "Jane Smith", "jane.smith@example.com");
    }
   
    @Test
        <UserModel> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);
        List<UserModel> result = userService.getAllUsers();
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
    }
    