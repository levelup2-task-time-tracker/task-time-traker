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
import java.util.regex.Pattern;


@ShellComponent
public class TimeCommand {

    @Autowired
    private ApiService api;

    @ShellMethod(key="time-start", value="Start tracking time for a task")
    public String startTime(@ShellOption(value = "--taskName", help = "The name of the task") String taskName){
        if (api.authenticate()) {
            return "You must login first.";
        }else{
            String taskId = getTaskUuid(taskName);
            if(taskId.contains("404")){
                return taskId;
            }else {
                var params = new HashMap<String, Object>();
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.POST, "time/" + taskId + "/start", params);
                return "Current time spent on task " + taskName + ": " + response.getBody();
            }
        }
    }

    @ShellMethod(key="time-stop", value="Stop tracking time for a task")
    public String stopTime(@ShellOption(value = "--taskName", help = "The name of the task") String taskName){
        if (api.authenticate()) {
            return "You must login first.";
        }else{
            String taskId = getTaskUuid(taskName);
            if(taskId.contains("404")){
                return taskId;
            }else {
                var params = new HashMap<String, Object>();
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.POST, "time/" + taskId + "/stop", params);
                return "Current time spent on task " + taskName + ": " + response.getBody();
            }
        }
    }

    @ShellMethod(key = "get-anomaly-logs", value = "Return the current server log of all anomalies. Only for project managers")
    public String getAnomalies(){
        if (api.authenticate()) {
            return "You must login first.";
        }else {
            ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET, "time/anomalies", new HashMap<>());
            List<String> parsedResponse = Pattern.compile(",")
                    .splitAsStream(response.getBody())
                    .toList();

            StringBuilder builder = new StringBuilder();

            for(String str: parsedResponse){
                builder.append(str).append("\n");
            }
            return builder.toString();
        }
    }

    private String getTaskUuid(String taskName){
        var params = new HashMap<String, Object>();
        params.put("taskName", taskName);
        ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET,"tasks/uuid", params);
        return response.getBody();
    }
}
