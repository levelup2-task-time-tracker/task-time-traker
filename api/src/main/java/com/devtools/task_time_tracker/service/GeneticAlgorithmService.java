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

@Service
public class GeneticAlgorithmService {

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ProjectRepository projectRepository;

    public List<AssignmentDto> getSuggestions(UUID projectId, String role) throws ResponseStatusException{
        Optional<ProjectModel> projectModelOptional = projectRepository.findById(projectId);
        if (projectModelOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        List<Object> resultSet = projectMemberRepository.sumByUser(projectId, role);
        List<User> users = resultSet.stream()
                .map(row -> {
                    Object[] columns = (Object[]) row;
                    UUID userId = (UUID) columns[0];
                    String userName = (String) columns[1];
                    double workLoad = ((BigDecimal) columns[2]).doubleValue();
                    return new User(userId, userName, workLoad);
                })
                .toList();

        resultSet = projectMemberRepository.sumByTask(projectId);
        List<Task> tasks = resultSet.stream()
                .map(row -> {
                    Object[] columns = (Object[]) row;
                    UUID taskId = (UUID) columns[0];
                    String taskName = (String) columns[1];
                    double estimatedRemainingTime = ((BigDecimal) columns[2]).doubleValue();
                    return new Task(taskId,estimatedRemainingTime, taskName);
                })
                .toList();

        Random random = new Random(0L);
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(users, tasks, random, 0.6, 0.4, 50, 20);
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
