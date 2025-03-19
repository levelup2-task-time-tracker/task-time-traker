package com.devtools.task_time_tracker_cli.component;

import org.springframework.stereotype.Component;

@Component
public class AuthToken {
    private String accessToken;
    private String accessCode;

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isAuthenticated() {
        return accessToken != null && !accessToken.isEmpty();
    }
}
