package com.devtools.task_time_tracker_cli.command;

import com.devtools.task_time_tracker_cli.component.AuthToken;
import com.devtools.task_time_tracker_cli.dto.TokenDto;
import com.devtools.task_time_tracker_cli.dto.UrlDto;
import org.springframework.http.*;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.awt.*;
import java.io.IOException;
import java.net.URI;


@ShellComponent
public class OAuthCommand {
    private final AuthToken authToken;
    private static final String AUTH_URL_ENDPOINT = "http://localhost:8080/auth/url";
    private static final String AUTH_TOKEN_ENDPOINT = "http://localhost:8080/auth/callback";
    private final RestTemplate restTemplate = new RestTemplate();

    public OAuthCommand(AuthToken token) {
        this.authToken = token;
    }

    @ShellMethod(key = "login", value = "Authenticate via Google OAuth2")
    public String login() {
        try {
            ResponseEntity<UrlDto> response = restTemplate.getForEntity(AUTH_URL_ENDPOINT, UrlDto.class);

            if (response.getBody() == null || response.getBody().authURL() == null) {
                return "Failed to retrieve authentication URL.";
            }

            String authUrl = response.getBody().authURL();
            openBrowser(authUrl);
            return "Opening browser for authentication: " + authUrl;
        } catch (Exception e) {
            return "Error retrieving authentication URL: " + e.getMessage();
        }
    }

    private void openBrowser(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(URI.create(url));
            } catch (IOException e) {
                System.out.println("Failed to open browser: " + e.getMessage());
            }
        } else {
            System.out.println("Please open this URL manually in your browser: " + url);
        }
    }

    @ShellMethod(key = "get-token", value = "Exchange auth code for access token")
    public String getToken(String authCode) {
        String url = UriComponentsBuilder.fromUriString(AUTH_TOKEN_ENDPOINT)
                .queryParam("code", authCode)
                .toUriString();
        try {
            ResponseEntity<TokenDto> response = restTemplate.getForEntity(url, TokenDto.class);

            if (response.getBody() != null && response.getBody().token() != null) {
                String accessToken = response.getBody().token();
                authToken.setAccessToken(accessToken);
                return "Authentication successful! Token stored.";
            }

            return "Failed to retrieve access token.";
        } catch (Exception e) {
            return "Error retrieving token: " + e.getMessage();
        }
    }

    @ShellMethod(key = "getAuthToken", value = "Return the auth token")
    public String getToken() {
        return authToken.getAccessToken();
    }
}
