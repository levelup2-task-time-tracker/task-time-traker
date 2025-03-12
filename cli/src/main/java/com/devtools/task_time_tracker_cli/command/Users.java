package com.devtools.task_time_tracker_cli.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

@ShellComponent
public class Users {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8080";

    @ShellMethod(key = "getUsers", value = "Returns the users")
    public String getUsers(){
        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "/users", String.class);
        return response.getBody();
    }
}
