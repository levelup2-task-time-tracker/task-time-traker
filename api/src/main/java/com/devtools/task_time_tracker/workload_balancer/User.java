package com.devtools.task_time_tracker.workload_balancer;

import java.util.UUID;

public class User {
    private final UUID userId;
    private final String userName;
    private final double workLoad;

    public User(UUID userId, String userName, double workLoad) {
        this.userId = userId;
        this.userName = userName;
        this.workLoad = workLoad;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public double getWorkLoad() {
        return workLoad;
    }
}
