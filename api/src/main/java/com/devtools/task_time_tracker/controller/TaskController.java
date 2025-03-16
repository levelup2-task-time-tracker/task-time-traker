package com.devtools.task_time_tracker.controller;

import com.devtools.task_time_tracker.model.TaskModel;
import com.devtools.task_time_tracker.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam Long projectId
    ) {
        TaskModel task = taskService.createTask(description, name, storyPoints, projectId);

        return ResponseEntity.ok(task);
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<TaskModel> updateTask(
            @PathVariable Long taskId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer storyPoints,
            @RequestParam(required = false) Long projectId
    ) {
        TaskModel task = taskService.updateTask(taskId, description, name, storyPoints, projectId);

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Boolean> deleteTask(@PathVariable Long taskId){
        return ResponseEntity.ok(taskService.deleteTask(taskId));
    }
}
