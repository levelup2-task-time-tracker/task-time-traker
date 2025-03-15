package com.devtools.task_time_tracker.controller;

import com.devtools.task_time_tracker.service.ProjectService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@PermitAll
@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

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