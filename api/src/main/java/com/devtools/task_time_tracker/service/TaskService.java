package com.devtools.task_time_tracker.service;

import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.TaskModel;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    public TaskModel createTask(String description, String name, Integer storyPoints, Long projectId) {
        return new TaskModel();
    }

    public  TaskModel updateTask(Long taskId, String newDescription, String newName, Integer storyPoints, Long projectId){
        return new TaskModel();
    }

    public Boolean deleteTask(Long taskId) {
        return true;
    }
}
