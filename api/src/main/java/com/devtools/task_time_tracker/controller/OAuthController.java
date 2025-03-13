package com.devtools.task_time_tracker.controller;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class OAuthController {


    private final String clientId = System.getenv("OAUTH2_ID");
    private final String clientSecret = System.getenv("OAUTH2_SECRET");
    private final String redirectUri = "http://localhost:8080/oauth/callback";

    @GetMapping("/callback")
    public String handleOAuth2Callback(@RequestParam("code") String authCode) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("code", authCode);
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        Map<String, String> tokenResponse = response.getBody();
        return tokenResponse.get("access_token");
    }

    @PostMapping("/token")
    public String getToken(){
        return "token";
    }
}
