package com.devtools.task_time_tracker_cli.command;

import com.devtools.task_time_tracker_cli.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.HashMap;


@ShellComponent
public class TimeCommand {

    @Autowired
    private ApiService api;

    @ShellMethod(key="time-start", value="Start tracking time for a task")
    public String startTime(String taskId){
        if (api.authenticate()) {
            return "You must login first.";
        }else{
            var params = new HashMap<String, Object>();
            ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.POST,"time/" + taskId + "/start", params);
            return "Current time spent on task " + taskId + ": " + response.getBody();
        }
    }

    @ShellMethod(key="time-stop", value="Stop tracking time for a task")
    public String stopTime(String taskId){
        if (api.authenticate()) {
            return "You must login first.";
        }else{
            var params = new HashMap<String, Object>();
            ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.POST,"time/" + taskId + "/stop", params);
            return "Current time spent on task " + taskId + ": " + response.getBody();
        }
    }
}
