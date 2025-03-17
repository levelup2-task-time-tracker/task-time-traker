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

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }


    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
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

    public ProjectModel(String name, String description, Boolean isPrivate) {
        this.isPrivate = isPrivate;
        this.name = name;
        this.description = description;
    }

    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;
    @Column(name = "is_private")
    private boolean isPrivate;
}