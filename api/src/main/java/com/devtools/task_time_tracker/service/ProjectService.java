package com.devtools.task_time_tracker.service;
import com.devtools.task_time_tracker.model.*;
import com.devtools.task_time_tracker.repository.*;
import com.devtools.task_time_tracker.utils.SharedFunctions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
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
    
    @Autowired
    private SharedFunctions sharedFunctions;


    @Transactional
    public ProjectModel createProject(String name, String description) throws ResponseStatusException{
        UserModel user = sharedFunctions.getLoggedInUser();

        ProjectModel project = new ProjectModel(name, description);
        projectRepository.save(project);
        RoleModel role = sharedFunctions.findRole("Manager");

        ProjectMemberModel projectMemberModel = new ProjectMemberModel(project, user, role);
        projectMemberRepository.save(projectMemberModel);

        return project;
    }

    public List<ProjectModel> getUserProjects() throws ResponseStatusException{
        UserModel user = sharedFunctions.getLoggedInUser();
        return projectRepository.findByUser(user.getUserId());
    }

    public ProjectModel updateProject(UUID projectId, String newDescription, String newName) throws ResponseStatusException{
        UserModel user = sharedFunctions.getLoggedInUser();
        ProjectModel project = sharedFunctions.findProject(projectId);
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
        ProjectModel project = sharedFunctions.findProject(projectId);
        UserModel user = sharedFunctions.getLoggedInUser();

        return userRepository.findByUserRoleFilter(user.getUserId(), roles);

    }



    public Boolean deleteProject(UUID projectId) throws ResponseStatusException{
        ProjectModel project = sharedFunctions.findProject(projectId);
        UserModel user = sharedFunctions.getLoggedInUser();
        verifyManager(user, project);
        projectRepository.delete(project);
        return true;
    }

    public Boolean addMember(UUID projectId, UUID userId, String role) throws ResponseStatusException{
        UserModel user = sharedFunctions.getLoggedInUser();
        ProjectModel project = sharedFunctions.findProject(projectId);
        verifyManager(user, project);
        UserModel userToAdd = sharedFunctions.findUser(userId);
        RoleModel userRole = sharedFunctions.findRole(role);
        Optional<ProjectMemberModel> projectMemberModelOptional = projectMemberRepository.findByUserAndProject(userToAdd, project);
        if (projectMemberModelOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are already part of project");
        }
        ProjectMemberModel projectMember = new ProjectMemberModel(project, userToAdd, userRole);
        projectMemberRepository.save(projectMember);

        return true;
    }

    public Boolean removeMember(UUID projectId, UUID userId) throws ResponseStatusException{
        UserModel user = sharedFunctions.getLoggedInUser();
        ProjectModel project = sharedFunctions.findProject(projectId);
        verifyManager(user, project);
        projectMemberRepository.deleteByUserAndProject(userId, projectId);

        return true;
    }

    public Boolean changeUserRole(UUID projectId, UUID userId, String role) throws ResponseStatusException {
        UserModel user = sharedFunctions.getLoggedInUser();
        ProjectModel project = sharedFunctions.findProject(projectId);
        verifyManager(user, project);
        UserModel userToUpdate = sharedFunctions.findUser(projectId);
        RoleModel userRole = sharedFunctions.findRole(role);
        Optional<ProjectMemberModel> projectMember = projectMemberRepository.findByUserAndProject(userToUpdate, project);
        if (projectMember.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not part of project");
        }
        projectMember.get().setRole(userRole);
        return true;
    }

    private  void verifyManager(UserModel user, ProjectModel project) throws  ResponseStatusException{
        RoleModel role = sharedFunctions.findRole("Manager");
        Optional<ProjectMemberModel> projectMemberModel = projectMemberRepository.findByUserAndProjectAndRole(user, project, role);
        if (projectMemberModel.isEmpty()){
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the manager of the project");
        }

    }

  public HashMap<String, Long> getTotalProjectTime(UUID projectId) throws ResponseStatusException {
        verifyUserProject(projectId);
        long totalProjectTimeInSeconds = projectRepository.getTotalProjectTimeInSeconds(projectId);

        return formatDurationToWorkingDaysHours(Duration.ofSeconds(totalProjectTimeInSeconds));
    }

    public static HashMap<String, Long> formatDurationToWorkingDaysHours(Duration duration) {

        HashMap<String, Long> durationMap = new HashMap<>();

        durationMap.put("days", duration.toHours() / 8);
        durationMap.put("hours", duration.toMinutes() % 60 == 0 ? duration.toHours() % 8 : duration.toHours() % 8 + 1);

        return durationMap;
    }

    public HashMap<String, HashMap<String, Long>> getProjectMembersTime(UUID projectId) throws ResponseStatusException {
        verifyUserProject(projectId);
        List<Object[]> results = projectRepository.getProjectMembersTimeInSeconds(projectId);

        HashMap<String, HashMap<String, Long>> projectMembersTimeMap = new HashMap<>();

        for (Object[] result : results) {
            String rowString = result[0].toString();

            rowString = rowString.substring(1, rowString.length() - 1);

            String[] dataInRow = rowString.split(",");

            HashMap<String, Long> workingDaysHoursMap = formatDurationToWorkingDaysHours(Duration.ofSeconds((long) Double.parseDouble(dataInRow[2]))); // Assuming total_seconds is at index 2
            projectMembersTimeMap.put(dataInRow[1].replaceAll("\"", ""), workingDaysHoursMap);
        }

        return projectMembersTimeMap;
    }

    public HashMap<String, HashMap<String, Long>> getProjectTasksTime(UUID projectId) throws ResponseStatusException {
        verifyUserProject(projectId);
        List<Object[]> results = projectRepository.getProjectTasksTimeInSeconds(projectId);

        HashMap<String, HashMap<String, Long>> projectTasksTimeMap = new HashMap<>();

        for (Object[] result : results) {
            String rowString = result[0].toString();

            rowString = rowString.substring(1, rowString.length() - 1);

            String[] dataInRow = rowString.split(",");

            HashMap<String, Long> workingDaysHoursMap = formatDurationToWorkingDaysHours(Duration.ofSeconds((long) Double.parseDouble(dataInRow[2]))); // Assuming total_seconds is at index 2
            projectTasksTimeMap.put(dataInRow[1].replaceAll("\"", ""), workingDaysHoursMap);
        }

        return projectTasksTimeMap;
    }

    public int getProjectTotalStoryPoints(UUID projectId, boolean completedOnly) throws ResponseStatusException {
        verifyUserProject(projectId);
        return projectRepository.getProjectTotalStoryPoints(projectId, completedOnly);
    }

    public HashMap<String, Long> getProjectAverageTimePerCompletedStoryPoints(UUID projectId) throws ResponseStatusException {
        verifyUserProject(projectId);
        return formatDurationToWorkingDaysHours(Duration.ofSeconds((long)projectRepository.getProjectAverageTimeInSecondsPerCompletedStoryPoints(projectId)));
    }

    private void verifyUserProject(UUID projectId) throws ResponseStatusException {
        UserModel user = sharedFunctions.getLoggedInUser();
        ProjectModel project = sharedFunctions.findProject(projectId);
        sharedFunctions.verifyUser(user,project);
    }

    public List<ProjectModel> getAll() {
        return projectRepository.findAll();
    }

    public String getUuidFromName(String projectName){
        return sharedFunctions.getProjectUuid(projectName).toString();
    }

    public Map<String, Object> completeProject(UUID projectId) throws ResponseStatusException{
        List<TaskModel> tasks = getTasks(projectId);
        boolean allTasksCompleted = tasks.stream().allMatch(task -> task.getCompletedAt() != null);
        Map<String, Object> response = new HashMap<>();

        if (allTasksCompleted) {
            ProjectModel project = sharedFunctions.findProject(projectId);

            if (project.getCompletedAt() != null) {
                response.put("success", false);
                response.put("message", "Project already completed");
            } else {

                project.setCompletedAt(LocalDateTime.now());

                projectRepository.save(project);

                response.put("success", true);
                response.put("message", "Completed project");
            }
        } else {
            response.put("success", false);
            response.put("message", "Not all tasks in project are completed");
        }

        return response;
    }
}
