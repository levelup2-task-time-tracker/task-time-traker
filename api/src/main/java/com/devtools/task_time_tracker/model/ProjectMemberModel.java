package com.devtools.task_time_tracker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "project_member")
public class ProjectMemberModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectMemberId;

    public ProjectMemberModel() {
    }

    public ProjectMemberModel(ProjectModel project, UserModel user, RoleModel role) {
        this.project = project;
        this.user = user;
        this.role = role;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectModel project;

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public ProjectModel getProject() {
        return project;
    }

    public void setProject(ProjectModel project) {
        this.project = project;
    }

    public RoleModel getRole() {
        return role;
    }

    public void setRole(RoleModel role) {
        this.role = role;
    }

    @ManyToOne
    @JoinColumn(name = "role_id")
    private  RoleModel role;
}
