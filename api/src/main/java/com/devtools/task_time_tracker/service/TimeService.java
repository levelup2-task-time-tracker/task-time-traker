package com.devtools.task_time_tracker.service;

import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.TaskModel;
import com.devtools.task_time_tracker.model.TimeLogModel;
import com.devtools.task_time_tracker.model.UserModel;
import com.devtools.task_time_tracker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.devtools.task_time_tracker.utils.SharedFunctions.*;

@Service
public class TimeService {

    @Autowired
    private TimeLogRepository timeLogRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public TimeLogModel startTime(UUID taskId) throws ResponseStatusException{
        UserModel user = getLoggedInUser(userRepository);
        verifyUserTask(taskId, user);

        TaskModel task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        List<TimeLogModel> timeLogModelOptional = timeLogRepository.findByUserAndTaskAndEndDateTimeIsNull(user, task);

        if (timeLogModelOptional.isEmpty()) {
            LocalDateTime startTime = LocalDateTime.now();
            TimeLogModel timeLogModel = new TimeLogModel(user, task, startTime);
            timeLogRepository.save(timeLogModel);

            return timeLogModel;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You are already logging time for the task");
        }
    }

    public TimeLogModel stopTime(UUID taskId) throws ResponseStatusException{
        UserModel user = getLoggedInUser(userRepository);
        TaskModel task = verifyUserTask(taskId, user);

        List<TimeLogModel> timeLogModelOptional = timeLogRepository.findByUserAndTaskAndEndDateTimeIsNull(user, task);

        if (timeLogModelOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No current log in progress for given task");
        }else{
            TimeLogModel timeLog = timeLogModelOptional.getFirst();
            LocalDateTime endTime = LocalDateTime.now();
            timeLog.setEndDateTime(endTime);
            timeLogRepository.save(timeLog);

            return timeLog;
        }
    }

    public Duration getTotalTime(UUID taskId) throws RuntimeException{
        TaskModel task = verifyUserTask(taskId, null);

        List<TimeLogModel> timeLog = timeLogRepository.findByTask(task);

        Duration totalDuration = timeLog.stream()
                .filter(log -> log.getEndDateTime() != null)
                .map(log -> Duration.between(log.getStartDateTime(), log.getEndDateTime()))
                .reduce(Duration.ZERO, Duration::plus);

        return totalDuration;
    }

    private TaskModel verifyUserTask(UUID taskId, UserModel user) throws RuntimeException {
        if (user == null) {
            user = getLoggedInUser(userRepository);
        }
        Optional<TaskModel> taskModelOptional = taskRepository.findById(taskId);
        if (taskModelOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        TaskModel task = taskModelOptional.get();
        ProjectModel project = findProject(task.getProject().getProjectId(), projectRepository);
        verifyUser(user, project, projectMemberRepository);

        return task;
    }

}
