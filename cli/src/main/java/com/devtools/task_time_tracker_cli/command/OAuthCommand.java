package com.devtools.task_time_tracker_cli.command;

import com.devtools.task_time_tracker_cli.component.AuthToken;
import org.springframework.http.*;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

@ShellComponent
public class OAuthCommand {
    private final AuthToken authToken;
    private static final String CLIENT_ID = System.getenv("OAUTH2_ID");
    private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String REDIRECT_URI = "http://localhost:8080/oauth/callback";
    private static final String SCOPE = "openid%20email%20profile";

    public OAuthCommand(AuthToken token) {
        this.authToken = token;
    }

    @ShellMethod(key = "login", value = "Authenticate via Google OAuth2")
    public String login() {
        try {
            String authRequestUrl = AUTH_URL +
                    "?client_id=" + CLIENT_ID +
                    "&redirect_uri=" + REDIRECT_URI +
                    "&response_type=code" +
                    "&scope=" + SCOPE +
                    "&access_type=offline" +
                    "&prompt=consent";

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI.create(authRequestUrl));
            } else {
                System.out.println("Open the following URL in your browser: " + authRequestUrl);
            }

            System.out.print("Enter the authorization code: ");
            Scanner scanner = new Scanner(System.in);
            String authCode = scanner.nextLine();

//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
//            requestBody.add("code", authCode);
//
//            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
//
//            ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/oauth/callback", requestEntity, String.class);
//
//            String accessToken = response.getBody();
            authToken.setAccessToken(authCode);

            return "Login successful! Access token obtained.";
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}
