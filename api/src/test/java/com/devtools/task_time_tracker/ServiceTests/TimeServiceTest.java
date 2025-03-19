//package com.devtools.task_time_tracker.ServiceTests;
//
//import com.devtools.task_time_tracker.model.TaskModel;
//import com.devtools.task_time_tracker.model.TimeLogModel;
//import com.devtools.task_time_tracker.repository.TaskRepository;
//import com.devtools.task_time_tracker.repository.TimeLogRepository;
//import com.devtools.task_time_tracker.repository.UserRepository;
//import com.devtools.task_time_tracker.service.TimeService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class TimeServiceTest {
//
//    @Mock
//    private TaskRepository taskRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private TimeService timeService;
//
//    private TaskModel task;
//    private TimeLogModel timeLog;
//
//    @BeforeEach
//    void setUp() {
//        task = new TaskModel();
////        task.setTaskId("1L");
//
////        timeLog = new TimeLogModel(task, LocalDateTime.now());
//        timeLog.setEndDateTime(LocalDateTime.now().plusHours(1));
//
//        when(taskRepository.findById(task.getTaskId())).thenReturn(Optional.of(task));
//    }
//
//    @Test
//    void testStartTime() {
////        when(timeLogRepository.findByTaskAndEndDateTimeIsNull(task)).thenReturn(Arrays.asList());
////
////        TimeLogModel result = timeService.startTime(task.getTaskId());
////
////        assertNotNull(result);
////        assertEquals(task, result.getTask());
////        verify(timeLogRepository, times(1)).save(any(TimeLogModel.class));
//    }
//
//    @Test
//    void testStartTimeAlreadyInProgress() {
////        when(timeLogRepository.findByTaskAndEndDateTimeIsNull(task)).thenReturn(Arrays.asList(timeLog));
////
////        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
////            timeService.startTime(task.getTaskId());
////        });
////
////        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
////        assertEquals("Already time log in progress for the given task", exception.getReason());
//    }
//
//    @Test
//    void testStopTime() {
////        when(timeLogRepository.findByTaskAndEndDateTimeIsNull(task)).thenReturn(Arrays.asList(timeLog));
////
////        TimeLogModel result = timeService.stopTime(task.getTaskId());
////
////        assertNotNull(result);
////        assertNotNull(result.getEndDateTime());
////        verify(timeLogRepository, times(1)).save(any(TimeLogModel.class));
//    }
//
//    @Test
//    void testStopTimeNoLogInProgress() {
////        when(timeLogRepository.findByTaskAndEndDateTimeIsNull(task)).thenReturn(Arrays.asList());
////
////        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
////            timeService.stopTime(task.getTaskId());
////        });
////
////        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
////        assertEquals("No current log in progress for given task", exception.getReason());
//    }
//
//    @Test
//    void testGetTotalTime() {
////        when(timeLogRepository.findByTask(task)).thenReturn(Arrays.asList(timeLog));
////
////        Duration totalTime = timeService.getTotalTime(task.getTaskId());
////
////        assertNotNull(totalTime);
////        assertEquals(Duration.ofHours(1), totalTime);
//    }
//}