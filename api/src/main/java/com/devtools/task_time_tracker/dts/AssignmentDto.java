package com.devtools.task_time_tracker.dts;

import com.devtools.task_time_tracker.workload_balancer.Task;
import com.devtools.task_time_tracker.workload_balancer.User;

import java.util.List;

public record AssignmentDto(User user, List<Task> tasks) {
}
