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

import java.util.Optional;
import java.util.UUID;

import static com.devtools.task_time_tracker.utils.SharedFunctions.*;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    public TaskModel createTask(String description, String name, Integer storyPoints, UUID projectId) throws  ResponseStatusException{
        UserModel user = getLoggedInUser(userRepository);
        ProjectModel project = findProject(projectId, projectRepository);
        verifyUser(user, project);
//        TaskModel task = new TaskModel(name, description, storyPoints, project);
//        taskRepository.save(task);
        return  null;
    }

    public  TaskModel updateTask(UUID taskId, String newDescription, String newName, Integer storyPoints, UUID projectId){
        return new TaskModel();
    }

    public Boolean deleteTask(UUID taskId) {
        return true;
    }

    private  void verifyUser(UserModel user, ProjectModel project) throws ResponseStatusException{
        Optional<ProjectMemberModel> projectMemberModel = projectMemberRepository.findByUserAndProject(user, project);
        if (projectMemberModel.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not part of project");
        }

    }
}
