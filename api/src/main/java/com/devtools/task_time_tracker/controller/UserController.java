package com.devtools.task_time_tracker.controller;

import com.devtools.task_time_tracker.model.UserModel;
import com.devtools.task_time_tracker.service.UserService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PermitAll
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<String> getAllUsers(@AuthenticationPrincipal OAuth2User user) {
        System.out.println(user);
        return ResponseEntity.ok("Working");
    }
}