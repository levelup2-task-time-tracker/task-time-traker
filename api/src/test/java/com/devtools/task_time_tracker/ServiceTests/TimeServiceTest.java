package com.devtools.task_time_tracker.ServiceTests;

import com.devtools.task_time_tracker.model.*;
import com.devtools.task_time_tracker.repository.*;
import com.devtools.task_time_tracker.utils.SharedFunctions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.devtools.task_time_tracker.service.TimeService;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeServiceTest {

    @Mock
    private TimeLogRepository timeLogRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SharedFunctions sharedFunctions;

    @InjectMocks
    private TimeService timeService;

    private UUID taskId;
    private TaskModel task;
    private UserModel user;
    private TimeLogModel timeLog;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        task = new TaskModel();
        task.setTaskId(taskId);
        user = new UserModel();
        user.setUserId(userId);
        LocalDateTime now = LocalDateTime.now();
        timeLog = new TimeLogModel(user, task, now);
    }

    @Test
    void startTime() {
        when(sharedFunctions.getLoggedInUser()).thenReturn(user);
        when(sharedFunctions.verifyUserTask(taskId, user)).thenReturn(task);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(timeLogRepository.findByUserAndTaskAndEndDateTimeIsNull(user, task)).thenReturn(new ArrayList<>());
        when(timeLogRepository.save(any(TimeLogModel.class))).thenReturn(timeLog);

        TimeLogModel result = timeService.startTime(taskId);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(task, result.getTask());
        assertNotNull(result.getStartDateTime());
        assertNull(result.getEndDateTime());

        verify(timeLogRepository, times(1)).save(any(TimeLogModel.class));
    }

    @Test
    void startTimeWhenTaskNotFound() {
        when(sharedFunctions.getLoggedInUser()).thenReturn(user);
        when(sharedFunctions.verifyUserTask(taskId, user)).thenReturn(task);
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> timeService.startTime(taskId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Task not found", exception.getReason());
    }

    @Test
    void startTimeWhenAlreadyLogging() {
        List<TimeLogModel> existingLogs = new ArrayList<>();
        existingLogs.add(timeLog);

        when(sharedFunctions.getLoggedInUser()).thenReturn(user);
        when(sharedFunctions.verifyUserTask(taskId, user)).thenReturn(task);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(timeLogRepository.findByUserAndTaskAndEndDateTimeIsNull(user, task)).thenReturn(existingLogs);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> timeService.startTime(taskId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("You are already logging time for the task", exception.getReason());
    }

    @Test
    void stopTime() {
        timeLog.setEndDateTime(null);
        List<TimeLogModel> existingLogs = new ArrayList<>();
        existingLogs.add(timeLog);

        when(sharedFunctions.getLoggedInUser()).thenReturn(user);
        when(sharedFunctions.verifyUserTask(taskId, user)).thenReturn(task);
        when(timeLogRepository.findByUserAndTaskAndEndDateTimeIsNull(user, task)).thenReturn(existingLogs);
        when(timeLogRepository.save(any(TimeLogModel.class))).thenReturn(timeLog);

        TimeLogModel result = timeService.stopTime(taskId);

        assertNotNull(result);
        assertNotNull(result.getEndDateTime());

        verify(timeLogRepository, times(1)).save(any(TimeLogModel.class));
    }

    @Test
    void stopTimeWhenNoLogInProgress() {
        when(sharedFunctions.getLoggedInUser()).thenReturn(user);
        when(sharedFunctions.verifyUserTask(taskId, user)).thenReturn(task);
        when(timeLogRepository.findByUserAndTaskAndEndDateTimeIsNull(user, task)).thenReturn(new ArrayList<>());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> timeService.stopTime(taskId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No current log in progress for given task", exception.getReason());
    }

    @Test
    void getTotalTime() {
        LocalDateTime start1 = LocalDateTime.now().minusHours(2);
        LocalDateTime end1 = LocalDateTime.now().minusHours(1);
        LocalDateTime start2 = LocalDateTime.now().minusMinutes(30);
        LocalDateTime end2 = LocalDateTime.now().minusMinutes(10);

        TimeLogModel log1 = new TimeLogModel(user, task, start1);
        log1.setEndDateTime(end1);
        TimeLogModel log2 = new TimeLogModel(user, task, start2);
        log2.setEndDateTime(end2);

        List<TimeLogModel> logs = new ArrayList<>();
        logs.add(log1);
        logs.add(log2);

        when(sharedFunctions.verifyUserTask(taskId, null)).thenReturn(task);
        when(timeLogRepository.findByTask(task)).thenReturn(logs);

        Duration totalDuration = timeService.getTotalTime(taskId);

        assertEquals(20, totalDuration.toMinutesPart());
        assertEquals(1, totalDuration.toHoursPart());
    }

    @Test
    void getTotalTimeWhenNoEndDateTime() {
        LocalDateTime start1 = LocalDateTime.now().minusHours(2);
        LocalDateTime end1 = LocalDateTime.now().minusHours(1);
        LocalDateTime start2 = LocalDateTime.now().minusMinutes(30);

        TimeLogModel log1 = new TimeLogModel(user, task, start1);
        log1.setEndDateTime(end1);
        TimeLogModel log2 = new TimeLogModel(user, task, start2);

        List<TimeLogModel> logs = new ArrayList<>();
        logs.add(log1);
        logs.add(log2);

        when(sharedFunctions.verifyUserTask(taskId, null)).thenReturn(task);
        when(timeLogRepository.findByTask(task)).thenReturn(logs);

        Duration totalDuration = timeService.getTotalTime(taskId);

        assertEquals(1, totalDuration.toHoursPart());
    }
}