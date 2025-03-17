package com.devtools.task_time_tracker.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class TimeLogModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeLogId;

    public TaskModel getTask() {
        return task;
    }

    public void setTask(TaskModel task) {
        this.task = task;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Long getTimeLogId() {
        return timeLogId;
    }

    public void setTimeLogId(Long timeLogId) {
        this.timeLogId = timeLogId;
    }

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskModel task;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
