package com.devtools.task_time_tracker_cli.component;

import org.springframework.stereotype.Component;

@Component
public class AuthToken {
    private String accessToken;

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
