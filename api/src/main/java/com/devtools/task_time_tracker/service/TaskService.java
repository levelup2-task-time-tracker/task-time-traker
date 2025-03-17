package com.devtools.task_time_tracker.service;

import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.TaskModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskService {

    public TaskModel createTask(String description, String name, Integer storyPoints, UUID projectId) {
        return new TaskModel();
    }

    public  TaskModel updateTask(UUID taskId, String newDescription, String newName, Integer storyPoints, UUID projectId){
        return new TaskModel();
    }

    public Boolean deleteTask(UUID taskId) {
        return true;
    }
}
