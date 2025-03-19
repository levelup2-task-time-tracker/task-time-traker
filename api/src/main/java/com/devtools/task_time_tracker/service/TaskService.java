package com.devtools.task_time_tracker.service;

import com.devtools.task_time_tracker.model.*;
import com.devtools.task_time_tracker.repository.ProjectMemberRepository;
import com.devtools.task_time_tracker.repository.ProjectRepository;
import com.devtools.task_time_tracker.repository.TaskRepository;
import com.devtools.task_time_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import static com.devtools.task_time_tracker.utils.SharedFunctions.*;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    public TaskModel createTask(String description, String name, Long storyPoints, UUID projectId) throws ResponseStatusException{
        ProjectModel project = findProject(projectId, projectRepository);
        UserModel user = getLoggedInUser(userRepository);
        verifyUser(user, project, projectMemberRepository);
        TaskModel task = new TaskModel(description, name, storyPoints, project);
        taskRepository.save(task);
        return task;
    }

    public  TaskModel updateTask(UUID taskId, String newDescription, String newName, Long storyPoints) throws ResponseStatusException{
        Optional<TaskModel> taskModelOptional = taskRepository.findById(taskId);
        if (taskModelOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        TaskModel task = taskModelOptional.get();
        ProjectModel project = findProject(task.getProject().getProjectId(), projectRepository);
        UserModel user = getLoggedInUser(userRepository);
        verifyUser(user, project, projectMemberRepository);

        if (newDescription != null){
            task.setDescription(newDescription);
        }
        if (newName != null){
            task.setName(newName);
        }
        if (storyPoints != null){
            task.setStoryPoints(storyPoints);
        }
        taskRepository.save(task);
        return task;
    }

    public Boolean deleteTask(UUID taskId) {
        Optional<TaskModel> taskModelOptional = taskRepository.findById(taskId);
        if (taskModelOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        TaskModel task = taskModelOptional.get();
        ProjectModel project = findProject(task.getProject().getProjectId(), projectRepository);
        UserModel user = getLoggedInUser(userRepository);
        verifyUser(user, project, projectMemberRepository);
        taskRepository.delete(task);
        return true;
    }

    public String getUuid(String taskName){
        return getUserTaskUuid(projectMemberRepository, taskRepository, taskName, getLoggedInUser(userRepository)).toString();
    }

    public Map<String, Object> completeTask(UUID taskId) throws ResponseStatusException{
        Map<String, Object> response = new HashMap<>();


        Optional<TaskModel> taskModelOptional = taskRepository.findById(taskId);
        if (taskModelOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        TaskModel task = taskModelOptional.get();

        if (task.getCompletedAt() != null) {
            response.put("success", false);
            response.put("message", "Task already completed");
        } else {
            task.setCompletedAt(LocalDateTime.now());

            taskRepository.save(task);

            response.put("success", true);
            response.put("message", "Completed project");
        }


        return response;
    }
}
