package com.devtools.task_time_tracker.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class TimeLogModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeLogId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskModel task;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
