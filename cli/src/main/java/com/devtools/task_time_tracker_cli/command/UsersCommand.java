package com.devtools.task_time_tracker_cli.command;

import com.devtools.task_time_tracker_cli.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.HashMap;

@ShellComponent
public class UsersCommand {
    @Autowired
    private ApiService api;

    @ShellMethod(key = "get-users", value = "Returns the users")
    public String getUsers(){
        if (api.authenticate()) {
            return "You must login first.";
        }

        ResponseEntity<String> response = api.getRequest("/users", new HashMap<>());

        return response.getBody();
    }
}
