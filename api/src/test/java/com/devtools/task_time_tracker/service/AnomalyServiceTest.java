package com.devtools.task_time_tracker.service;

import com.devtools.task_time_tracker.model.TaskModel;
import com.devtools.task_time_tracker.model.TimeLogModel;
import com.devtools.task_time_tracker.model.UserModel;
import com.devtools.task_time_tracker.repository.TaskRepository;
import com.devtools.task_time_tracker.repository.TimeLogRepository;
import com.devtools.task_time_tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnomalyServiceTest {
    @Mock
    private TimeLogRepository timeLogRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AnomalyService anomalyService;

    private UserModel testUser;
    private final List<TaskModel> testTasks = new ArrayList<>();
    private TimeLogModel timeLog;

    @BeforeEach
    void setUp() {
        testUser = new UserModel();
        testUser.setName("Test User");
        for(int i = 0; i < 4; i++){
            testTasks.add(new TaskModel());
        }
    }

    @Test
    void testDetectAnomalies_NoTimeLogged() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));
        when(timeLogRepository.findByUser(testUser)).thenReturn(Collections.emptyList());

        anomalyService.runDetectAnomalies();

        assertFalse(anomalyService.getAnomalyLogs().isEmpty());
        assertTrue(anomalyService.getAnomalyLogs().get(0).contains("no time logged"));
    }

    @Test
    void testDetectAnomalies_OverworkStreak() {
        List<TimeLogModel> logs = createOverworkStreakLogs();
        when(userRepository.findAll()).thenReturn(List.of(testUser));
        when(timeLogRepository.findByUser(testUser)).thenReturn(logs);

        anomalyService.runDetectAnomalies();

        assertTrue(anomalyService.getAnomalyLogs().stream()
                .anyMatch(log -> log.contains("overwork streak")));
    }

    @Test
    void testDetectAnomalies_InactivityStreak() {
        List<TimeLogModel> logs = createInactivityStreakLogs();
        when(userRepository.findAll()).thenReturn(List.of(testUser));
        when(timeLogRepository.findByUser(testUser)).thenReturn(logs);

        anomalyService.runDetectAnomalies();

        assertTrue(anomalyService.getAnomalyLogs().stream()
                .anyMatch(log -> log.contains("inactivity streak")));
    }

    @Test
    void testCheckMonthlyWorkPatterns_IrregularPattern() {
        List<TimeLogModel> logs = createIrregularWorkPatternLogs();
        Map<LocalDateTime, Duration> workHours = anomalyService.calculateDailyWorkHours(logs);

        anomalyService.checkMonthlyWorkPatterns(workHours, testUser);

        assertTrue(anomalyService.getAnomalyLogs().stream()
                .anyMatch(log -> log.contains("irregular work pattern")));
    }

    @Test
    void testCheckHealthyBehaviour_UnusualWorkHours() {
        TimeLogModel log = createTimeLogAtHour(3);
        anomalyService.checkHealthyBehaviour(List.of(log), testUser);

        assertTrue(anomalyService.getAnomalyLogs().stream()
                .anyMatch(logEntry -> logEntry.contains("unusual work hours")));
    }

    @Test
    void testCheckHealthyBehaviour_LongSession() {
        TimeLogModel log = createTimeLogWithDuration(5);
        anomalyService.checkHealthyBehaviour(List.of(log), testUser);

        assertTrue(anomalyService.getAnomalyLogs().stream()
                .anyMatch(logEntry -> logEntry.contains("long continuous work session")));
    }

    @Test
    void testMultiTaskingCheck_TaskSwitchingFatigue() {
        List<TimeLogModel> logs = createShortTaskLogs();

        anomalyService.multiTaskingCheck(logs, testUser);

        assertTrue(anomalyService.getAnomalyLogs().stream()
                .anyMatch(log -> log.contains("task switching fatigue")));
    }

    private List<TimeLogModel> createOverworkStreakLogs() {
        return List.of(createTimeLogOnDate(14, LocalDate.now().minusDays(3)),
                createTimeLogOnDate(13, LocalDate.now().minusDays(2)),
                createTimeLogOnDate(16, LocalDate.now().minusDays(1)),
                createTimeLogOnDate(18, LocalDate.now()));
    }

    private List<TimeLogModel> createInactivityStreakLogs() {
        return List.of(createTimeLogOnDate(1, LocalDate.now().minusDays(7)),
                createTimeLogOnDate(7, LocalDate.now()));
    }

    private List<TimeLogModel> createIrregularWorkPatternLogs() {
        return List.of(createTimeLogOnDate(3, LocalDate.now().minusDays(5)),
                createTimeLogOnDate(12, LocalDate.now().minusDays(4)),
                createTimeLogOnDate(1, LocalDate.now().minusDays(3)),
                createTimeLogOnDate(14, LocalDate.now().minusDays(2)),
                createTimeLogOnDate(14, LocalDate.now().minusDays(6)),
                createTimeLogOnDate(14, LocalDate.now().minusDays(2)));
    }

    private List<TimeLogModel> createShortTaskLogs() {
        return List.of(createTimeLogOnDate(1, LocalDate.now()),
                createTimeLogAtHour(1),
                createTimeLogAtHour(2),
                createTimeLogAtHour(3),
                createTimeLogAtHour(4),
                createTimeLogAtHour(5));
    }

    private TimeLogModel createTimeLogWithDuration(int durationHours) {
        TimeLogModel log = new TimeLogModel();
        log.setUser(testUser);
        log.setStartDateTime(LocalDateTime.now().minusHours(durationHours));
        log.setEndDateTime(LocalDateTime.now());
        return log;
    }

    private TimeLogModel createTimeLogAtHour(int hour) {
        TimeLogModel log = new TimeLogModel();
        log.setUser(testUser);
        log.setStartDateTime(LocalDateTime.now().withHour(hour));
        log.setEndDateTime(log.getStartDateTime().plusHours(2));
        return log;
    }

    private TimeLogModel createTimeLogOnDate(int durationHours, LocalDate date) {
        TimeLogModel log = new TimeLogModel();
        log.setUser(testUser);
        log.setStartDateTime(date.atStartOfDay().plusHours(9));
        log.setEndDateTime(log.getStartDateTime().plusHours(durationHours));
        return log;
    }
}
