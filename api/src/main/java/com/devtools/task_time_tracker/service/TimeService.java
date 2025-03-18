package com.devtools.task_time_tracker.service;

import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.model.TaskModel;
import com.devtools.task_time_tracker.model.TimeLogModel;
import com.devtools.task_time_tracker.model.UserModel;
import com.devtools.task_time_tracker.repository.TaskRepository;
import com.devtools.task_time_tracker.repository.TimeLogRepository;
import com.devtools.task_time_tracker.repository.UserRepository;
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

import static com.devtools.task_time_tracker.utils.SharedFunctions.getLoggedInUser;

@Service
public class TimeService {

    @Autowired
    private TimeLogRepository timeLogRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public TimeLogModel startTime(UUID taskId) throws ResponseStatusException{
        UserModel user = getLoggedInUser(userRepository);

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

        TaskModel task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

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

    public Duration getTotalTime(UUID taskId){
        TaskModel task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        List<TimeLogModel> timeLog = timeLogRepository.findByTask(task);

        Duration totalDuration = timeLog.stream()
                .filter(log -> log.getEndDateTime() != null)
                .map(log -> Duration.between(log.getStartDateTime(), log.getEndDateTime()))
                .reduce(Duration.ZERO, Duration::plus);

        return totalDuration;
    }
}
