package com.devtools.task_time_tracker.service;
import com.devtools.task_time_tracker.model.*;
import com.devtools.task_time_tracker.repository.*;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

import static com.devtools.task_time_tracker.utils.SharedFunctions.findProject;
import static com.devtools.task_time_tracker.utils.SharedFunctions.getLoggedInUser;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Transactional
    public ProjectModel createProject(String name, String description, Boolean isPrivate) throws ResponseStatusException{
        UserModel user = getLoggedInUser(userRepository);
        UUID projectId = UUID.randomUUID();

        ProjectModel project = new ProjectModel(name, description, isPrivate);
        projectRepository.save(project);

        Optional<RoleModel> roleModelOptional = roleRepository.findByRoleName("Manager");
        if (roleModelOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role manager not found");
        }

        ProjectMemberModel projectMemberModel = new ProjectMemberModel(project, user, roleModelOptional.get());
        projectMemberRepository.save(projectMemberModel);

        return project;
    }

    public List<ProjectModel> getUserProjects() throws ResponseStatusException{
        UserModel user = getLoggedInUser(userRepository);
        return projectRepository.findByUser(user.getUserId());
    }

    public List<ProjectModel> getAllProjects() throws ResponseStatusException{
        return projectRepository.findByIsPrivateFalse();
    }

    public ProjectModel updateProject(UUID projectId, String newDescription, String newName, Boolean isPrivate) throws ResponseStatusException{
        UserModel user = getLoggedInUser(userRepository);
        ProjectModel project = findProject(projectId, projectRepository);

        Optional<ProjectMemberModel> projectMember = projectMemberRepository.findByUserAndProject(user,project);
        if (projectMember.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }

        if (newDescription != null) {
            project.setDescription(newDescription);
        }

        if (newName != null) {
            project.setName(newName);
        }

        if (isPrivate != null) {
            project.setPrivate(isPrivate);
        }

        projectRepository.save(project);

        return project;

    }

    public  List<TaskModel> getTasks(UUID projectId) throws ResponseStatusException{
        Optional<ProjectModel> project = projectRepository.findById(projectId);

        if (project.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        return taskRepository.findByProject(project.get());
    }

    public List<UserModel> getUsers(UUID projectId, String roles) throws ResponseStatusException{
        ProjectModel project = findProject(projectId, projectRepository);
        UserModel user = getLoggedInUser(userRepository);

        if (project.isPrivate()){
            Optional<ProjectMemberModel> projectMemberModel = projectMemberRepository.findByUserAndProject(user, project);
            if (projectMemberModel.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
            }
        }

        return userRepository.findByUserRoleFilter(user.getUserId(), roles);

    }



    public Boolean deleteProject(UUID projectId) throws ResponseStatusException{
        ProjectModel project = findProject(projectId, projectRepository);
        UserModel user = getLoggedInUser(userRepository);
        Optional<ProjectMemberModel> projectMember = projectMemberRepository.findByUserAndProject(user,project);
        if (projectMember.isEmpty() || !projectMember.get().getRole().getRoleName().equals("Manger")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        project.setDeletedAt(currentDateTime);
        projectRepository.save(project);
        return true;
    }

    public Boolean restoreProject(UUID projectId) throws ResponseStatusException{
        ProjectModel project = findProject(projectId, projectRepository);
        UserModel user = getLoggedInUser(userRepository);
        Optional<ProjectMemberModel> projectMember = projectMemberRepository.findByUserAndProject(user,project);
        if (projectMember.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }
        project.setDeletedAt(null);
        projectRepository.save(project);
        return true;
    }

    public Boolean addMember(UUID projectId, UUID userId, String role) {
        return true;
    }

    public Boolean removeMember(UUID projectId, UUID userId) {
        return  true;
    }

    public Boolean addRoleToMember(UUID projectId, UUID userId, String roleName) {
        return true;
    }

    public Boolean removeRoleFromMember(UUID projectId, UUID userId) {
        return true;
    }
}
