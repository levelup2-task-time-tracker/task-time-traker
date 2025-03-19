package com.devtools.task_time_tracker_cli.controller;
import com.devtools.task_time_tracker_cli.component.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {

    @Autowired
    private AuthToken token;

    @GetMapping("/auth/code")
    public String displayAuthCode(@RequestParam("code") String code) {
        token.setAccessCode(code);
        return "<html><body>"
                + "<h2>Return to the CLI and type: validate</h2>"
//                + "<p><strong>" + code + "</strong></p>"
                + "<p>Enjoy!</p>"
                + "</body></html>";
    }
}
