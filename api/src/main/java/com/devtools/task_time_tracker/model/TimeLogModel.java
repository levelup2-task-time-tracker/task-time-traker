package com.devtools.task_time_tracker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "time_log")
public class TimeLogModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeLogId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskModel task;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public TimeLogModel(){}

    public TimeLogModel(TaskModel task, LocalDateTime startDateTime){
        this.task = task;
        this.startDateTime = startDateTime;
        this.endDateTime = null;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
}
