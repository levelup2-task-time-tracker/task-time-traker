package com.devtools.task_time_tracker.model;

import jakarta.persistence.*;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "task")
public class TaskModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID taskId;

    private String name;

    public TaskModel(){}

    public TaskModel(String description, String name, Long storyPoints, ProjectModel project){
        this.description = description;
        this.name = name;
        this.storyPoints = storyPoints;
        this.project = project;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(Long storyPoints) {
        this.storyPoints = storyPoints;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public ProjectModel getProject() {
        return project;
    }

    public void setProject(ProjectModel project) {
        this.project = project;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectModel project;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Column(name = "story_points", columnDefinition = "TIMESTAMP")
    private Long storyPoints;
}
