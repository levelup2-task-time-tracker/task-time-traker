package com.devtools.task_time_tracker_cli.command;

import com.devtools.task_time_tracker_cli.component.AuthToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;


@ShellComponent
public class UsersCommand {
    private final AuthToken authToken;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8080";

    public UsersCommand(AuthToken token) {
        this.authToken = token;
    }

    @ShellMethod(key = "getUsers", value = "Returns the users")
    public String getUsers(){
        if (!authToken.isAuthenticated()) {
            return "You must login first.";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken.getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
}
