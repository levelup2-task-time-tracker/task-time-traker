package com.devtools.task_time_tracker.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "project")
public class ProjectModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID projectId;
    private String name;

    public UUID getProjectId() {
        return projectId;
    }

    public ProjectModel() {
    }


    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getCompletedAt() {
        return this.completedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectModel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "completed_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime completedAt;
}