package com.devtools.task_time_tracker_cli.command;

import com.devtools.task_time_tracker_cli.component.AuthToken;
import com.devtools.task_time_tracker_cli.dto.TokenDto;
import com.devtools.task_time_tracker_cli.dto.UrlDto;
import org.springframework.http.*;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
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
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                Runtime.getRuntime().exec("open " + url);
            } else {
                Runtime.getRuntime().exec("xdg-open " + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ShellMethod(key = "validate", value = "Validate auth code for access to the API")
    public String validate(String authCode) {
        String url = UriComponentsBuilder.fromUriString(AUTH_TOKEN_ENDPOINT)
                .queryParam("code", authCode)
                .toUriString();
        try {
            ResponseEntity<TokenDto> response = restTemplate.getForEntity(url, TokenDto.class);

            if (response.getBody() != null && response.getBody().token() != null) {
                String accessToken = response.getBody().token();
                authToken.setAccessToken(accessToken);
                return "Auth code valid and authentication successful!";
            }

            return "Authentication failed";
        } catch (Exception e) {
            return "Error retrieving token: " + e.getMessage();
        }
    }

    @ShellMethod(key = "getAuthToken", value = "Return the auth token")
    public String getToken() {
        return authToken.getAccessToken();
    }
}
