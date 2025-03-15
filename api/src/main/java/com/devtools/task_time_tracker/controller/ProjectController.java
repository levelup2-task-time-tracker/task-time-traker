package com.devtools.task_time_tracker.controller;

import com.devtools.task_time_tracker.service.ProjectService;
import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.TaskModel;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @PostMapping
    public ResponseEntity<ProjectModel> createProject(@RequestParam String description) {
        ProjectModel project = projectService.createProject(description);

        return ResponseEntity.ok(project);
    }


    @GetMapping
    public ResponseEntity<List<ProjectModel>> getUserProjects() {
        List<ProjectModel> projects = projectService.getUserProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjectModel>> getAllProjects() {
        List<ProjectModel> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/{projectId}")
    public ResponseEntity<ProjectModel> updateProject(
            @PathVariable Long projectId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String newManagerSubject) {
        ProjectModel project = projectService.updateProject(projectId, description, newManagerSubject);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<TaskModel>> getTasks(@PathVariable Long projectId) {
        List<TaskModel> tasks = projectService.getTasks(projectId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{projectId}/users")
    public ResponseEntity<List<TaskModel>> getUsers(@PathVariable Long projectId) {
        List<TaskModel> tasks = projectService.getUsers(projectId);
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Boolean> deleteProject(@PathVariable Long projectId){
        return ResponseEntity.ok(projectService.deleteProject(projectId));
    }
    
    @GetMapping("/projects/{projectId}/time")
    public ResponseEntity<Double> getTotalTimeSpent(@AuthenticationPrincipal OAuth2User user, @PathVariable String projectId) {
        return ResponseEntity.ok(0.0);
    }

    @GetMapping("/projects/{projectId}/time/person")
    public ResponseEntity<Double> getTimeSpentPerPerson(@AuthenticationPrincipal OAuth2User user, @PathVariable String projectId) {
        return ResponseEntity.ok(0.0);
    }

    @GetMapping("/projects/{projectId}/time/task")
    public ResponseEntity<Double> getTimeSpentPerTask(@AuthenticationPrincipal OAuth2User user, @PathVariable String projectId) {
        return ResponseEntity.ok(0.0);
    }

    @GetMapping("/projects/{projectId}/time/days_per_point")
    public ResponseEntity<Double> getAvgDayPerStoryPoint(@AuthenticationPrincipal OAuth2User user, @PathVariable String projectId) {
        return ResponseEntity.ok(0.0);
    }

    @GetMapping("/projects/{projectId}/story_points")
    public ResponseEntity<Double> getTotalStoryPoint(@AuthenticationPrincipal OAuth2User user, @PathVariable String projectId) {
        return ResponseEntity.ok(0.0);
    }
}
