package com.devtools.task_time_tracker.controller;


import com.devtools.task_time_tracker.dts.AssignmentDto;
import com.devtools.task_time_tracker.service.GeneticAlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/GA")
public class GeneticAlgorithmController {
    @Autowired
    private GeneticAlgorithmService geneticAlgorithmService;

    @GetMapping("/recommendations/{projectId}")
    public ResponseEntity<List<AssignmentDto>> getRecommendation(@PathVariable UUID projectId) {
        return ResponseEntity.ok(geneticAlgorithmService.getRecommendations(projectId));
    }
}
