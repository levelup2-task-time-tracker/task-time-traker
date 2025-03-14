package com.devtools.task_time_tracker_cli.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {
    @GetMapping("/auth/code")
    public String displayAuthCode(@RequestParam("code") String code) {
        return "<html><body>"
                + "<h2>Copy this Authorization Code:</h2>"
                + "<p><strong>" + code + "</strong></p>"
                + "<p>Paste this code in your Spring Shell application.</p>"
                + "</body></html>";
    }
}
