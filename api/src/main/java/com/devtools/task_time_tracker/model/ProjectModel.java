package com.devtools.task_time_tracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "project")
public class ProjectModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    public ProjectModel() {
    }

    public ProjectModel(UserModel manager, String description) {
        this.manager = manager;
        this.description = description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setManager(UserModel manager) {
        this.manager = manager;
    }

    private String description;

    public UserModel getManager() {
        return manager;
    }

    public String getDescription() {
        return description;
    }

    public Long getProjectId() {
        return projectId;
    }

    @ManyToOne
    @JoinColumn(name = "manager")
    private UserModel manager;
}