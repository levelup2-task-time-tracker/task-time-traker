package com.devtools.task_time_tracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TimeLogModelTest {

    private TimeLogModel timeLog;
    private TaskModel task;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @BeforeEach
           task = new TaskModel();
        startDateTime = LocalDateTime.now();
        endDateTime = LocalDateTime.now().plusHours(1);
        timeLog = new TimeLogModel(task, startDateTime);
        timeLog.setEndDateTime(endDateTime);
    }

    @Test
    void testConstructor() {
        TimeLogModel newTimeLog = new TimeLogModel(task, startDateTime);
        assertNotNull(newTimeLog);
        assertEquals(task, newTimeLog.getTask());
        assertEquals(startDateTime, newTimeLog.getStartDateTime());
        assertEquals(null, newTimeLog.getEndDateTime());
    }

    @Test
    void testSettersAndGetters() {
        TaskModel newTask = new TaskModel();
        LocalDateTime newStartDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime newEndDateTime = LocalDateTime.now().plusDays(1).plusHours(1);

        timeLog.setTask(newTask);
        timeLog.setStartDateTime(newStartDateTime);
        timeLog.setEndDateTime(newEndDateTime);

        assertEquals(newTask, timeLog.getTask());
        assertEquals(newStartDateTime, timeLog.getStartDateTime());
        assertEquals(newEndDateTime, timeLog.getEndDateTime());
    }
}