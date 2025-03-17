package com.devtools.task_time_tracker.controller;

import com.devtools.task_time_tracker.model.TaskModel;
import com.devtools.task_time_tracker.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskModel> createTask(
            @RequestParam String description,
            @RequestParam String name,
            @RequestParam Integer storyPoints,
            @RequestParam UUID projectId
    ) {
        TaskModel task = taskService.createTask(description, name, storyPoints, projectId);

        return ResponseEntity.ok(task);
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<TaskModel> updateTask(
            @PathVariable UUID taskId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer storyPoints,
            @RequestParam(required = false) UUID projectId
    ) {
        TaskModel task = taskService.updateTask(taskId, description, name, storyPoints, projectId);

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Boolean> deleteTask(@PathVariable UUID taskId){
        return ResponseEntity.ok(taskService.deleteTask(taskId));
    }
}
