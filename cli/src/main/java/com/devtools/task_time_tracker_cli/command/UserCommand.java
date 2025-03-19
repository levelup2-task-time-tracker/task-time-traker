package com.devtools.task_time_tracker_cli.command;

import com.devtools.task_time_tracker_cli.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ShellComponent
public class UserCommand {
    @Autowired
    private ApiService api;

    @ShellMethod(key = "get-users", value = "Returns the users")
    public String getUsers(){
        if (api.authenticate()) {
            return "You must login first.";
        }else{
            ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET,"users", new HashMap<>());
            List<Map<String, Object>> parsedBody = api.jsonArrayHandler(response);
            return api.displayResponseArray(parsedBody, "All projects");
        }
    }

    @ShellMethod(key = "get-user-info", value = "Returns all user info i.e. all projects they work on, all tasks, all time logs")
    public String userInfo(@ShellOption(value = "--userId", help = "Id of the user") String userId, @ShellOption(value = "--project", help = "Name of the project") String project){
        if (api.authenticate()) {
            return "You must login first.";
        }else{
            var params = new HashMap<String, Object>();
            params.put("projectId", project);
            ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET,"users/" + userId, params);
            return response.getBody();
        }
    }
}
