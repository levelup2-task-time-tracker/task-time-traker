package com.devtools.task_time_tracker.controller;

import com.devtools.task_time_tracker.model.UserModel;
import com.devtools.task_time_tracker.service.UserService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PermitAll
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<String> getAllUsers(@AuthenticationPrincipal OAuth2User user) {
        return ResponseEntity.ok("Working");
    }

    @GetMapping("/users/{user_id}")
    public ResponseEntity<String> getUser(@AuthenticationPrincipal OAuth2User user, @PathVariable String userId, @RequestParam String projectId) {
        // This will be user info, i.e. all projects they work on, all tasks, all time logs
        return ResponseEntity.ok("Working");
    }
}