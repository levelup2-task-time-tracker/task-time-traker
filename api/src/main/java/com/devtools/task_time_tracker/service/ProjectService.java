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

import static com.devtools.task_time_tracker.utils.SharedFunctions.*;

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
    public ProjectModel createProject(String name, String description) throws ResponseStatusException{
        UserModel user = getLoggedInUser(userRepository);
        UUID projectId = UUID.randomUUID();

        ProjectModel project = new ProjectModel(name, description);
        projectRepository.save(project);
        RoleModel role = findRole("Manager", roleRepository);

        ProjectMemberModel projectMemberModel = new ProjectMemberModel(project, user, role);
        projectMemberRepository.save(projectMemberModel);

        return project;
    }

    public List<ProjectModel> getUserProjects() throws ResponseStatusException{
        UserModel user = getLoggedInUser(userRepository);
        return projectRepository.findByUser(user.getUserId());
    }

    public ProjectModel updateProject(UUID projectId, String newDescription, String newName) throws ResponseStatusException{
        UserModel user = getLoggedInUser(userRepository);
        ProjectModel project = findProject(projectId, projectRepository);
        verifyManager(user, project);

        if (newDescription != null) {
            project.setDescription(newDescription);
        }

        if (newName != null) {
            project.setName(newName);
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

        return userRepository.findByUserRoleFilter(user.getUserId(), roles);

    }



    public Boolean deleteProject(UUID projectId) throws ResponseStatusException{
        ProjectModel project = findProject(projectId, projectRepository);
        UserModel user = getLoggedInUser(userRepository);
        verifyManager(user, project);
        LocalDateTime currentDateTime = LocalDateTime.now();
        project.setDeletedAt(currentDateTime);
        projectRepository.save(project);
        return true;
    }

    public Boolean restoreProject(UUID projectId) throws ResponseStatusException{
        ProjectModel project = findProject(projectId, projectRepository);
        UserModel user = getLoggedInUser(userRepository);
        verifyManager(user, project);
        project.setDeletedAt(null);
        projectRepository.save(project);
        return true;
    }

    public Boolean addMember(UUID projectId, UUID userId, String role) throws ResponseStatusException{
        UserModel user = getLoggedInUser(userRepository);
        ProjectModel project = findProject(projectId, projectRepository);
        verifyManager(user, project);
        UserModel userToAdd = findUser(userId, userRepository);
        RoleModel userRole = findRole(role, roleRepository);
        ProjectMemberModel projectMember = new ProjectMemberModel(project, userToAdd, userRole);
        projectMemberRepository.save(projectMember);

        return true;
    }

    public Boolean removeMember(UUID projectId, UUID userId) throws ResponseStatusException{
        UserModel user = getLoggedInUser(userRepository);
        ProjectModel project = findProject(projectId, projectRepository);
        verifyManager(user, project);
        UserModel userToRemove = findUser(userId, userRepository);
        projectMemberRepository.deleteByUserAndProject(userToRemove, project);

        return true;
    }

    public Boolean changeUserRole(UUID projectId, UUID userId, String role) throws ResponseStatusException {
        UserModel user = getLoggedInUser(userRepository);
        ProjectModel project = findProject(projectId, projectRepository);
        verifyManager(user, project);
        UserModel userToUpdate = findUser(userId, userRepository);
        RoleModel userRole = findRole(role, roleRepository);
        Optional<ProjectMemberModel> projectMember = projectMemberRepository.findByUserAndProject(userToUpdate, project);
        if (projectMember.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not part of project");
        }
        projectMember.get().setRole(userRole);
        return true;
    }

    private  void verifyManager(UserModel user, ProjectModel project) throws  ResponseStatusException{
        RoleModel role = findRole("Manager", roleRepository);
        Optional<ProjectMemberModel> projectMemberModel = projectMemberRepository.findByUserAndProjectAndRole(user, project, role);
        if (projectMemberModel.isEmpty()){
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the manager of the project");
        }

    }

}
