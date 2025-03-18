package com.devtools.task_time_tracker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "time_log")
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public TimeLogModel() {
    }

    public TimeLogModel(UserModel user, TaskModel task, LocalDateTime startDateTime) {
        this.user = user;
        this.task = task;
        this.startDateTime = startDateTime;
        this.endDateTime = null;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
