package com.devtools.task_time_tracker.workload_balancer;

import java.util.UUID;

public class Task {
    private final UUID taskId;
    private final String taskName;
    private final Long storyPoints;

    public Task(UUID taskId, Long estimatedTime, String taskName) {
        this.taskId = taskId;
        this.storyPoints = estimatedTime;
        this.taskName = taskName;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public Long getStoryPoints() {
        return storyPoints;
    }
}
