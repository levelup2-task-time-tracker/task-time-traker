package com.devtools.task_time_tracker_cli.command;

import com.devtools.task_time_tracker_cli.service.ApiService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ShellComponent
public class ProjectCommand {

    @Autowired
    private ApiService api;

    @ShellMethod(key = "create-project", value = "Create a new project")
    public String createProject(@ShellOption(value = "--name", help = "Name of the project") String name, @ShellOption(value = "--description", help = "A short description of the project") String description){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            var params = new HashMap<String, Object>();
            params.put("description", description);
            params.put("name", name);
            ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.POST,"projects", params);
            return response.getBody();
        }
    }

    @ShellMethod(key="get-user-projects", value="Get all your projects")
    public String getUserProjects(){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET,"projects", new HashMap<>());
            List<Map<String, Object>> parsedBody = api.jsonArrayHandler(response);
            return api.displayResponseArray(parsedBody, "Your projects");
        }
    }

    @ShellMethod(key="get-all-projects", value="Get all projects available")
    public String getAllProjects(){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET,"projects/all", new HashMap<>());
            List<Map<String, Object>> parsedBody = api.jsonArrayHandler(response);
            return api.displayResponseArray(parsedBody, "All projects");
        }
    }

    @ShellMethod(key = "update-project", value = "Update a specific project")
    public String updateProject(@ShellOption(value = "--projectName", help = "The name of the project") String projectName,
                                @ShellOption(value = "--newDescription", help = "The new description of the project") String description,
                                @ShellOption(value = "--newName", help = "The new name of the project") String name)
    {
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String projectId = getProjectUuid(projectName);
            if(projectId.contains("404")){
               return projectId;
            }else{
                var params = new HashMap<String, Object>();
                params.put("description", description);
                params.put("name", name);
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.POST,"projects/" + projectId, params);
                return response.getBody();
            }
        }
    }

    @ShellMethod(key = "get-project-users", value = "Get all users working on a specific project")
    public String getProjectUsers(String projectName){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String projectId = getProjectUuid(projectName);
            if(projectId.contains("404")){
                return projectId;
            }else{
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET,"projects/" + projectId + "/users", new HashMap<>());
                return response.getBody();
            }
        }
    }

    @ShellMethod(key = "get-project-tasks", value = "Get all tasks of a specific project")
    public String getProjectTasks(@ShellOption(value = "--projectName", help = "The name of the project") String projectName){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String projectId = getProjectUuid(projectName);
            if(projectId.contains("404")){
                return projectId;
            }else{
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET,"projects/" + projectId + "/tasks", new HashMap<>());
                List<Map<String, Object>> parsedBody = api.jsonArrayHandler(response);
                return api.displayResponseArray(parsedBody, "Tasks for project: " + projectName);
            }
        }
    }

    @ShellMethod(key = "delete-project", value = "Delete a specific project")
    public String deleteProject(@ShellOption(value = "--projectName", help = "The name of the project") String projectName){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String projectId = getProjectUuid(projectName);
            if(projectId.contains("404")){
                return projectId;
            }else{
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.DELETE,"projects/" + projectId, new HashMap<>());
                return response.getBody();
            }
        }
    }

    @ShellMethod(key = "get-project-time", value = "Get total time spent on a project")
    public String getProjectTime(@ShellOption(value = "--projectName", help = "The name of the project") String projectName){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String projectId = getProjectUuid(projectName);
            if(projectId.contains("404")){
                return projectId;
            }else{
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET,"projects/" + projectId + "/time", new HashMap<>());

                JSONObject jsonObject = new JSONObject(response.getBody());

                return "Total time spent on project is: " + jsonObject.get("days") + " working day(s), " + jsonObject.get("hours") + " hours.";
            }
        }
    }

    @ShellMethod(key = "get-user-time", value = "Get total time spent by a user on a project")
    public String getUserProjectTime(@ShellOption(value = "--projectName", help = "The name of the project") String projectName){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String projectId = getProjectUuid(projectName);
            if(projectId.contains("404")){
                return projectId;
            }else{
                StringBuilder returnSb = new StringBuilder();

                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET,"projects/" + projectId + "/time/person", new HashMap<>());

                JSONObject outerJsonObject = new JSONObject(response.getBody());


                Iterator<String> outerKeys = outerJsonObject.keys();
                while (outerKeys.hasNext()) {
                    String userName = outerKeys.next();
                    JSONObject innerJsonObject = outerJsonObject.getJSONObject(userName);

                    returnSb.append(userName + " - "+ innerJsonObject.get("days") + " working day(s), " + innerJsonObject.get("hours") + " hours.\n");
                }

                return returnSb.toString();
            }
        }
    }

    @ShellMethod(key = "get-task-time", value = "Get total time spent on a task of a project")
    public String getTaskProjectTime(@ShellOption(value = "--projectName", help = "The name of the project") String projectName){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String projectId = getProjectUuid(projectName);
            if(projectId.contains("404")){
                return projectId;
            }else {
                StringBuilder returnSb = new StringBuilder();

                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET, "projects/" + projectId + "/time/task", new HashMap<>());

                JSONObject outerJsonObject = new JSONObject(response.getBody());

                Iterator<String> outerKeys = outerJsonObject.keys();
                while (outerKeys.hasNext()) {
                    String taskName = outerKeys.next();
                    JSONObject innerJsonObject = outerJsonObject.getJSONObject(taskName);

                    returnSb.append(taskName + " - " + innerJsonObject.get("days") + " working day(s), " + innerJsonObject.get("hours") + " hours.\n");
                }

                return returnSb.toString();
            }
        }
    }

    @ShellMethod(key = "get-project-story-points", value = "Get total time spent per point")
    public String getTotalPerPoint(@ShellOption(value = "--projectName", help = "The name of the project") String projectName){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String projectId = getProjectUuid(projectName);
            if(projectId.contains("404")){
                return projectId;
            }else {
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET, "projects/" + projectId + "/story_points", new HashMap<>());
                return response.getBody();
            }
        }
    }

    @ShellMethod(key = "get-project-completed-tasks-story-points", value = "Get total time spent per point")
    public String getTotalCompletedTasksStoryPoints(@ShellOption(value = "--projectName", help = "The name of the project") String projectName){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String projectId = getProjectUuid(projectName);
            if(projectId.contains("404")){
                return projectId;
            }else {
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET, "projects/" + projectId + "/story_points/completed", new HashMap<>());
                return response.getBody();
            }
        }
    }

    @ShellMethod(key = "get-avg-completed-time-per-point", value = "Get average time spent per point")
    public String getDaysPerPoint(@ShellOption(value = "--projectName", help = "The name of the project") String projectName){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String projectId = getProjectUuid(projectName);
            if(projectId.contains("404")){
                return projectId;
            }else {
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET, "projects/" + projectId + "/time/avg_per_completed_point", new HashMap<>());
                return response.getBody();
            }
        }
    }

    @ShellMethod(key = "workload-suggestions", value = "Get workload suggestions")
    public String updateTask(@ShellOption(value = "--projectName", help = "The name of the project") String projectName){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String projectId = getProjectUuid(projectName);
            if(projectId.contains("404")){
                return projectId;
            }else {
                var params = new HashMap<String, Object>();
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.GET, "projects/"+projectId+"/suggestions", params);
                return response.getBody();
            }
        }
    }

    @ShellMethod(key = "add-member", value = "Add member to project")
    public String addMember(
            String userId,
            String projectName,
            @ShellOption(value = "--role", defaultValue = "Developer") String role) {
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String projectId = getProjectUuid(projectName);
            if(projectId.contains("404")){
                return projectId;
            }else {
                var params = new HashMap<String, Object>();
                params.put("role", role);
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.POST, "projects/"+ projectId+"/add_member/"+userId, params);
                return response.getBody();
            }
        }
    }

    @ShellMethod(key = "remove-member", value = "Remove member to project")
    public String removeMember(String userId, String projectName){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String projectId = getProjectUuid(projectName);
            if(projectId.contains("404")){
                return projectId;
            }else {
                var params = new HashMap<String, Object>();
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.DELETE, "projects/"+ projectId+"/remove_member/"+userId, params);
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

    @ShellMethod(key = "complete-project", value = "Complete a specific project")
    public String completeProject(@ShellOption(value = "--projectName", help = "The name of the project") String projectName){
        if(api.authenticate()){
            return "You must login first.";
        }else{
            String projectId = getProjectUuid(projectName);
            if(projectId.contains("404")){
                return projectId;
            }else{
                ResponseEntity<String> response = api.sendRequest(String.class, HttpMethod.POST,"projects/" + projectId + "/complete", new HashMap<>());
                return response.getBody();
            }
        }
    }

}
