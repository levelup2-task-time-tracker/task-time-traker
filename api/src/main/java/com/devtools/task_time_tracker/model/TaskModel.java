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

    public TaskModel(String description, String name, Long storyPoints, ProjectModel project, RoleModel role){
        this.description = description;
        this.name = name;
        this.storyPoints = storyPoints;
        this.project = project;
        this.roleType = role;
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

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public ProjectModel getProject() {
        return project;
    }

    public void setProject(ProjectModel project) {
        this.project = project;
    }

    public RoleModel getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleModel user) {
        this.roleType = user;
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
    @JoinColumn(name = "role_type")
    private RoleModel roleType;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectModel project;

    @Column(name = "completed_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime completedAt;

    @Column(name = "story_points", columnDefinition = "TIMESTAMP")
    private Long storyPoints;
}
