package com.devtools.task_time_tracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserRoleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRoleId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleModel role;
}
