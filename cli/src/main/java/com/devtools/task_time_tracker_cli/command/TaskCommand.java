package com.devtools.task_time_tracker_cli.command;

import com.devtools.task_time_tracker_cli.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.HashMap;

@ShellComponent
public class TaskCommand {

    @Autowired
    private ApiService api;

    @ShellMethod(key = "create-task", value = "Create a task for a project")
    public String createTask(String name, String description, String points, String projectId){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            var params = new HashMap<String, Object>();
            params.put("name", name);
            params.put("description", description);
            params.put("storyPoints", points);
            params.put("projectId", projectId);
            ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.POST,"tasks", params);
            return response.getBody();
        }
    }

    @ShellMethod(key = "update-task", value = "Update a task")
    public String updateTask(String taskName, String newName, String description, String points){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String taskId = getTaskUuid(taskName);
            if(taskId.contains("404")){
                return taskId;
            }else {
                var params = new HashMap<String, Object>();
                params.put("name", newName);
                params.put("description", description);
                params.put("storyPoints", points);
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.POST,"tasks/" + taskId, params);
                return response.getBody();
            }
        }
    }

    @ShellMethod(key = "delete-task", value = "Delete a task")
    public String deleteTask(String taskName){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String taskId = getTaskUuid(taskName);
            if(taskId.contains("404")){
                return taskId;
            }else {
                var params = new HashMap<String, Object>();
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.DELETE, "tasks/" + taskId, params);
                return response.getBody();
            }
        }
    }

    private String getTaskUuid(String taskName){
        var params = new HashMap<String, Object>();
        params.put("taskName", taskName);
        ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET,"tasks/uuid", params);
        return response.getBody();
    }

}
