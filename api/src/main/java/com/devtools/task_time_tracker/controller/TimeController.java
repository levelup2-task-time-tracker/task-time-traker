package com.devtools.task_time_tracker.controller;

import com.devtools.task_time_tracker.model.TimeLogModel;
import com.devtools.task_time_tracker.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/time")
public class TimeController {

    @Autowired
    private TimeService timeService;

    // Time Started
    // Current Time Logged


    @PostMapping("/{taskId}/start")
    public ResponseEntity<String> startTime(
            @AuthenticationPrincipal OAuth2User user,
            @PathVariable Long taskId
    ){
        TimeLogModel timeLogModel = timeService.startTime(taskId);

        Duration currentTimeSpent = timeService.getTotalTime(taskId);
        long hours = currentTimeSpent.toHours();
        long minutes = currentTimeSpent.toMinutesPart();
        long seconds = currentTimeSpent.toSecondsPart();

        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return ResponseEntity.ok(formattedTime);
    }

    @PostMapping("/{taskId}/stop")
    public ResponseEntity<String> stopTime(
            @AuthenticationPrincipal OAuth2User user,
            @PathVariable Long taskId
    ){
        TimeLogModel timeLogModel = timeService.stopTime(taskId);
        Duration currentTimeSpent = timeService.getTotalTime(taskId);
        long hours = currentTimeSpent.toHours();
        long minutes = currentTimeSpent.toMinutesPart();
        long seconds = currentTimeSpent.toSecondsPart();

        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return ResponseEntity.ok(formattedTime);
    }
}
