package com.devtools.task_time_tracker.controller;

import com.devtools.task_time_tracker.dts.AssignmentDto;
import com.devtools.task_time_tracker.model.TaskModel;
import com.devtools.task_time_tracker.service.GeneticAlgorithmService;
import com.devtools.task_time_tracker.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
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
            @RequestParam Long storyPoints,
            @RequestParam UUID projectId,
            @RequestParam(required = false, defaultValue = "Developer") String role
    ) {
        TaskModel task = taskService.createTask(description, name, storyPoints, projectId, role);

        return ResponseEntity.ok(task);
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<TaskModel> updateTask(
            @PathVariable UUID taskId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long storyPoints,
            @RequestParam(required = false) String role
    ) {
        TaskModel task = taskService.updateTask(taskId, description, name, storyPoints, role);

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Boolean> deleteTask(@PathVariable UUID taskId){
        return ResponseEntity.ok(taskService.deleteTask(taskId));
    }

    @GetMapping("/uuid")
    public ResponseEntity<String> getUuid(
            @AuthenticationPrincipal OAuth2User user,
            @RequestParam String taskName
    ){
        try{
            return ResponseEntity.ok(taskService.getUuid(taskName));
        }catch (ResponseStatusException e){
            return ResponseEntity.ok(e.getLocalizedMessage());
        }
    }

    @PostMapping("/{taskId}/complete")
    public ResponseEntity<Map<String, Object>> completeTask(
            @PathVariable UUID taskId
    ) {
        return ResponseEntity.ok(taskService.completeTask(taskId));
    }
}
