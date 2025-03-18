package com.devtools.task_time_tracker.controller;

import com.devtools.task_time_tracker.model.UserModel;
import com.devtools.task_time_tracker.service.ProjectService;
import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.TaskModel;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @PostMapping
    public ResponseEntity<ProjectModel> createProject(
            @RequestParam String description,
            @RequestParam String name
    ) {
        ProjectModel project = projectService.createProject(name, description);
        return ResponseEntity.ok(project);
    }

    @GetMapping
    public ResponseEntity<List<ProjectModel>> getUserProjects() {
        List<ProjectModel> projects = projectService.getUserProjects();
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/{projectId}")
    public ResponseEntity<ProjectModel> updateProject(
            @PathVariable UUID projectId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isPrivate
    ) {
        ProjectModel project = projectService.updateProject(projectId, description, name);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<TaskModel>> getTasks(@PathVariable UUID projectId) {
        List<TaskModel> tasks = projectService.getTasks(projectId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{projectId}/users")
    public ResponseEntity<List<UserModel>> getUsers(@PathVariable UUID projectId, @RequestParam(required = false) String roles) {
        List<UserModel> tasks = projectService.getUsers(projectId, roles);
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Boolean> deleteProject(@PathVariable UUID projectId){
        return ResponseEntity.ok(projectService.deleteProject(projectId));
    }

    @PostMapping("/{projectId}/restore")
    public ResponseEntity<Boolean> restoreProject(@PathVariable UUID projectId){
        return ResponseEntity.ok(projectService.restoreProject(projectId));
    }

    @PostMapping("/{projectId}/add_member/{userId}")
    public ResponseEntity<Boolean> addMember(
            @PathVariable UUID projectId,
            @PathVariable UUID userId,
            @RequestParam(required = false, defaultValue = "Contributor") String role
    ) {
        return ResponseEntity.ok(projectService.addMember(projectId, userId, role));
    }

    @DeleteMapping("/{projectId}/remove_member/{userId}")
    public ResponseEntity<Boolean> removeMember(
            @PathVariable UUID projectId,
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(projectService.removeMember(projectId, userId));
    }

    @PostMapping("/{projectId}/change_role/{userId}")
    public ResponseEntity<Boolean> changeRole(
            @PathVariable UUID projectId,
            @PathVariable UUID userId,
            @RequestParam(required = false, defaultValue = "Contributor") String role
    ) {
        return ResponseEntity.ok(projectService.changeUserRole(projectId, userId, role));
    }


    @GetMapping("/{projectId}/time")
    public ResponseEntity<Double> getTotalTimeSpent(@AuthenticationPrincipal OAuth2User user, @PathVariable String projectId) {
        return ResponseEntity.ok(0.0);
    }

    @GetMapping("/{projectId}/time/person")
    public ResponseEntity<Double> getTimeSpentPerPerson(@AuthenticationPrincipal OAuth2User user, @PathVariable String projectId) {
        return ResponseEntity.ok(0.0);
    }

    @GetMapping("/{projectId}/time/task")
    public ResponseEntity<Double> getTimeSpentPerTask(@AuthenticationPrincipal OAuth2User user, @PathVariable String projectId) {
        return ResponseEntity.ok(0.0);
    }

    @GetMapping("/{projectId}/time/days_per_point")
    public ResponseEntity<Double> getAvgDayPerStoryPoint(@AuthenticationPrincipal OAuth2User user, @PathVariable String projectId) {
        return ResponseEntity.ok(0.0);
    }

    @GetMapping("/{projectId}/story_points")
    public ResponseEntity<Double> getTotalStoryPoint(@AuthenticationPrincipal OAuth2User user, @PathVariable String projectId) {
        return ResponseEntity.ok(0.0);
    }
}
