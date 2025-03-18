package com.devtools.task_time_tracker.service;

import com.devtools.task_time_tracker.dts.AssignmentDto;
import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.TaskModel;
import com.devtools.task_time_tracker.repository.*;
import com.devtools.task_time_tracker.workload_balancer.Chromosome;
import com.devtools.task_time_tracker.workload_balancer.GeneticAlgorithm;
import com.devtools.task_time_tracker.workload_balancer.Task;
import com.devtools.task_time_tracker.workload_balancer.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GeneticAlgorithmService {

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ProjectRepository projectRepository;

    public List<AssignmentDto> getRecommendations(UUID projectId) throws ResponseStatusException{
        Optional<ProjectModel> projectModelOptional = projectRepository.findById(projectId);
        if (projectModelOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        List<Object> resultSet = projectMemberRepository.sumByUser(projectId);
        List<User> users = resultSet.stream()
                .map(row -> {
                    Object[] columns = (Object[]) row;
                    UUID userId = (UUID) columns[0];
                    String userName = (String) columns[1];
                    double workLoad = ((BigDecimal) columns[2]).doubleValue();
                    return new User(userId, userName, workLoad);
                })
                .toList();

        List<TaskModel> taskModels = taskRepository.findByProject(projectModelOptional.get());

        List<Task> tasks = taskModels.stream().map(taskModel -> {
            return new Task(
                    taskModel.getTaskId(),
                    taskModel.getStoryPoints(),
                    taskModel.getName()
            );
        }).toList();

        Random random = new Random(100);
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(users, tasks, random, 0.5, 0.5, 100, 50);
        Chromosome bestChromosome = geneticAlgorithm.run();
        List<Integer> assignments = bestChromosome.getAssignments();
        Map<Integer, AssignmentDto> assignmentMap = new HashMap<>();

        for (int i = 0; i < assignments.size(); i++) {
            int userIndex = assignments.get(i);
            AssignmentDto existingDto = assignmentMap.get(userIndex);

            if (existingDto == null) {
                List<Task> userTasks = new ArrayList<>();
                userTasks.add(tasks.get(i));
                assignmentMap.put(userIndex, new AssignmentDto(users.get(userIndex), userTasks));
            } else {
                existingDto.tasks().add(tasks.get(i));
            }
        }


        return new ArrayList<>(assignmentMap.values());
    }
}
