package com.devtools.task_time_tracker.service;

import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.TaskModel;
import com.devtools.task_time_tracker.repository.ProjectRepository;
import com.devtools.task_time_tracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public TaskModel createTask(String description, String name, Long storyPoints, UUID projectId) {
        Optional<ProjectModel> project = projectRepository.findById(projectId);
        if(project.isPresent()){
            TaskModel task = new TaskModel(description, name, storyPoints, project.get());
            taskRepository.save(task);
            return task;
        }else {
            return new TaskModel();

        }
    }

    public  TaskModel updateTask(UUID taskId, String newDescription, String newName, Integer storyPoints, UUID projectId){
        return new TaskModel();
    }

    public Boolean deleteTask(UUID taskId) {
        return true;
    }
}
