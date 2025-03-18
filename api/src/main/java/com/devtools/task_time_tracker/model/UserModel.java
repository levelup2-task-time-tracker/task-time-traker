package com.devtools.task_time_tracker.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Entity
@Table(name = "app_user")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    private String subject;

    public UserModel(String name, String subject) {
        this.name = name;
        this.subject = subject;
    }

    public UserModel() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    private String name;


}