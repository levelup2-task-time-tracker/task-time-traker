package com.devtools.task_time_tracker_cli.command;

import com.devtools.task_time_tracker_cli.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.HashMap;

@ShellComponent
public class TaskCommand {

    @Autowired
    private ApiService api;

    @ShellMethod(key = "create-task", value = "Create a task for a project")
    public String createTask(
            @ShellOption(value = "--name", help = "The name of the task") String name,
            @ShellOption(value = "--description", help = "Short description of the task") String description,
            @ShellOption(value = "--point", help = "The story points for the task") String points,
            @ShellOption(value = "--projectName", help = "The name of the project") String projectName,
            @ShellOption(value = "--role", help = "The role type of the project", defaultValue = "Developer") String role
    ) {
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String projectId = getProjectUuid(projectName);
            if(projectId.contains("404")){
                return projectId;
            }else {
            var params = new HashMap<String, Object>();
            params.put("name", name);
            params.put("description", description);
            params.put("storyPoints", points);
            params.put("projectId", projectId);
            params.put("role", role);
            ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.POST,"tasks", params);
            return response.getBody();
            }
        }
    }

    @ShellMethod(key = "update-task", value = "Update a task")
    public String updateTask(
            @ShellOption(value = "--taskName", help = "The name of the task") String taskName,
            @ShellOption(value = "--description", help = "New short description of the task") String description,
            @ShellOption(value = "--points", help = "The new story points for the task") String points,
            @ShellOption(value = "--newName", help = "The new name of the task") String newName,
            @ShellOption(value = "--role", help = "The role type of the project", defaultValue = "Developer") String role
    ){
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
                params.put("role", role);
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.POST,"tasks/" + taskId, params);
                return response.getBody();
            }
        }
    }

    @ShellMethod(key = "delete-task", value = "Delete a task")
    public String deleteTask(@ShellOption(value = "--taskName", help = "The name of the task") String taskName){
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

    @ShellMethod(key = "complete-task", value = "Complete a specific task")
    public String completeTask(@ShellOption(value = "--taskName", help = "The name of the task") String taskName){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String taskId = getTaskUuid(taskName);
            if(taskId.contains("404")){
                return taskId;
            }else {
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.POST, "tasks/" + taskId + "/complete", new HashMap<>());
                return response.getBody();
            }
        }
    }

    private String getProjectUuid(String projectName){
        var params = new HashMap<String, Object>();
        params.put("projectName", projectName);
        ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET,"projects/uuid", params);
        return response.getBody();
    }
}
