package com.devtools.task_time_tracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ProjectModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private String description;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private UserModel manager;
}