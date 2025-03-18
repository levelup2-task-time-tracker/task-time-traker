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
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
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

    @GetMapping("/all")
    public ResponseEntity<List<ProjectModel>> getProjects() {
        List<ProjectModel> tasks = projectService.getAll();
        return ResponseEntity.ok(tasks);
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

    @PostMapping("/{projectId}/add_member")
    public ResponseEntity<Boolean> addMember(
            @PathVariable UUID projectId,
            @RequestParam UUID userId,
            @RequestParam(required = false, defaultValue = "Developer") String role
    ) {
        return ResponseEntity.ok(projectService.addMember(projectId, userId, role));
    }

    @DeleteMapping("/{projectId}/remove_member")
    public ResponseEntity<Boolean> removeMember(
            @PathVariable UUID projectId,
            @RequestParam UUID userId
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
    public ResponseEntity<HashMap<String, Long>> getTotalProjectTime(@PathVariable UUID projectId) {
        return ResponseEntity.ok(projectService.getTotalProjectTime(projectId));
    }

    @GetMapping("/{projectId}/time/person")
    public ResponseEntity<HashMap<String, HashMap<String, Long>>> getTimeSpentPerPerson(@AuthenticationPrincipal OAuth2User user, @PathVariable UUID projectId) {
        return ResponseEntity.ok(projectService.getProjectMembersTime(projectId));
    }

    @GetMapping("/{projectId}/time/task")
    public ResponseEntity<HashMap<String, HashMap<String, Long>>> getTimeSpentPerTask(@AuthenticationPrincipal OAuth2User user, @PathVariable UUID projectId) {
        return ResponseEntity.ok(projectService.getProjectTasksTime(projectId));
    }

    @GetMapping("/{projectId}/story_points")
    public ResponseEntity<Integer> getTotalStoryPoint(@AuthenticationPrincipal OAuth2User user, @PathVariable UUID projectId) {
        return ResponseEntity.ok(projectService.getProjectTotalStoryPoints(projectId, false));
    }


    @GetMapping("/{projectId}/story_points/completed")
    public ResponseEntity<Integer> getTotalCompletedTasksStoryPoint(@AuthenticationPrincipal OAuth2User user, @PathVariable UUID projectId) {
        return ResponseEntity.ok(projectService.getProjectTotalStoryPoints(projectId, true));
    }

    @GetMapping("/{projectId}/time/avg_per_completed_point")
    public ResponseEntity<HashMap<String, Long>> getProjectAverageTimePerCompletedStoryPoints(@AuthenticationPrincipal OAuth2User user, @PathVariable UUID projectId) {
        return ResponseEntity.ok(projectService.getProjectAverageTimePerCompletedStoryPoints(projectId));
    }
}
