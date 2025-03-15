package com.devtools.task_time_tracker.service;
import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.TaskModel;
import com.devtools.task_time_tracker.model.UserModel;
import com.devtools.task_time_tracker.repository.ProjectRepository;
import com.devtools.task_time_tracker.repository.TaskRepository;
import com.devtools.task_time_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import static com.devtools.task_time_tracker.utils.SharedFunctions.getLoggedInUser;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;


    public ProjectModel createProject(String description) throws ResponseStatusException{

        UserModel user = getLoggedInUser(userRepository);

        ProjectModel project = new ProjectModel(user, description);
        projectRepository.save(project);

        return project;
    }

    public List<ProjectModel> getUserProjects() throws ResponseStatusException{
        UserModel user = getLoggedInUser(userRepository);
        return projectRepository.findByManager(user);
    }

    public List<ProjectModel> getAllProjects() throws ResponseStatusException{
        return projectRepository.findAll();
    }

    public ProjectModel updateProject(Long projectId, String newDescription, String newManagerSubject) throws ResponseStatusException{
        UserModel user = getLoggedInUser(userRepository);

        Optional<ProjectModel> projectModelOptional = projectRepository.findById(projectId);

        if (projectModelOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        ProjectModel project = projectModelOptional.get();

        if (!user.getUserId().equals(project.getManager().getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this project.");
        }

        if (newDescription != null) {
            project.setDescription(newDescription);
        }

        if (newManagerSubject != null) {
            Optional<UserModel> newManager = userRepository.findBySubject(newManagerSubject);
            newManager.ifPresent(project::setManager);
        }

        projectRepository.save(project);

        return project;

    }

    public  List<TaskModel> getTasks(Long projectId) throws ResponseStatusException{
        Optional<ProjectModel> project = projectRepository.findById(projectId);

        if (project.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        return taskRepository.findByProject(project.get());
    }

    public List<TaskModel> getUsers(Long projectId) {
        return new ArrayList<>();
    }

    public Boolean deleteProject(Long projectId) {
        return  true;
    }
}
