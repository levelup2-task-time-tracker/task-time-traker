package com.devtools.task_time_tracker.workload_balancer;

import java.util.UUID;

public class Task {
    private final UUID taskId;
    private final String taskName;
    private final Double   estimatedRemainingTime;

    public Task(UUID taskId, Double  estimatedRemainingTime, String taskName) {
        this.taskId = taskId;
        this.  estimatedRemainingTime =  estimatedRemainingTime;
        this.taskName = taskName;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public Double getEstimatedRemainingTime() {
        return   estimatedRemainingTime;
    }
}
