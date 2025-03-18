package com.devtools.task_time_tracker.controller;

import com.devtools.task_time_tracker.dts.AssignmentDto;
import com.devtools.task_time_tracker.model.TaskModel;
import com.devtools.task_time_tracker.service.GeneticAlgorithmService;
import com.devtools.task_time_tracker.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private GeneticAlgorithmService geneticAlgorithmService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/suggestions/{projectId}")
    public ResponseEntity<List<AssignmentDto>> getSuggestions(@PathVariable UUID projectId, @RequestParam(required = false, defaultValue = "Developer") String role) {
        return ResponseEntity.ok(geneticAlgorithmService.getSuggestions(projectId, role));
    }

    @PostMapping
    public ResponseEntity<TaskModel> createTask(
            @RequestParam String description,
            @RequestParam String name,
            @RequestParam Long storyPoints,
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
