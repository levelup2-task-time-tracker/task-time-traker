package com.devtools.task_time_tracker.service;

import com.devtools.task_time_tracker.model.*;
import com.devtools.task_time_tracker.repository.*;
import com.devtools.task_time_tracker.workload_balancer.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.devtools.task_time_tracker.utils.SharedFunctions.*;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnomalyService {

    @Autowired
    private TimeLogRepository timeLogRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired ProjectRepository projectRepository;

    @Scheduled(fixedRate = 3000000)
    public void runDetectAnomalies(){
        detectAnomalies();
    }
    private final List<String> anomalyLogs = new ArrayList<>();

    public List<String> getAnomalyLogs() {
        return anomalyLogs;
    }

    public List<String> getProjectAnomalyLogs() {
        UserModel user = getLoggedInUser(userRepository);
        RoleModel role = findRole("Manager", roleRepository);
        Optional<ProjectMemberModel> memberRole = projectMemberRepository.findByUserAndRole(user, role);

        if (memberRole.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not a project manager");
        }

        List<ProjectModel> projects = projectRepository.findByUser(user.getUserId());
        Set<String> memberNames = projects.stream()
                .flatMap(project -> projectMemberRepository.findByProject(project).stream())
                .map(member -> member.getUser().getName())
                .collect(Collectors.toSet());

        return anomalyLogs.stream()
                .filter(log -> memberNames.stream().anyMatch(log::contains))
                .collect(Collectors.toList());
    }

    private void detectAnomalies() {
        List<UserModel> users = userRepository.findAll();
        for (UserModel user : users) {
            List<TimeLogModel> logs = timeLogRepository.findByUser(user);
            if (!logs.isEmpty()) {
                Map<LocalDateTime, Duration> dailyTimeWorked = calculateDailyWorkHours(logs);
                this.checkHealthyBehaviour(logs, user);
                this.multiTaskingCheck(logs, user);
                this.checkMonthlyWorkPatterns(dailyTimeWorked, user);
                this.checkStreaks(dailyTimeWorked, user);
            }else{
                LocalDateTime tmstamp = LocalDateTime.now();
                logAnomaly(user, "no time logged", tmstamp.toString());
            }
        }
    }

    public void logAnomaly(UserModel user, String type, String details) {
        String alert = "[Anomaly Alert] " + user.getName() + " - " + type + ": " + details;
        anomalyLogs.add(alert);
    }

    public void checkStreaks(Map<LocalDateTime, Duration> dailyTimeWorked, UserModel user){
        List<LocalDate> sortedDates = new ArrayList<>(dailyTimeWorked.keySet().stream()
                .map(LocalDateTime::toLocalDate)
                .sorted()
                .toList());

        int streak = 0;
        for (int i = 1; i < sortedDates.size(); i++) {
            LocalDate prev = sortedDates.get(i - 1);
            LocalDate curr = sortedDates.get(i);

            if (ChronoUnit.DAYS.between(prev, curr) == 1) {
                streak++;
                if (dailyTimeWorked.get(curr.atStartOfDay()).toHours() > 12 && streak >= 3) {
                    logAnomaly(user, "overwork streak", "Working over 12h/day for " + streak + " days");
                }
            } else {
                streak = 0;
            }
        }

        int inactivityStreak = 0;
        for (int i = 1; i < sortedDates.size(); i++) {
            LocalDate prev = sortedDates.get(i - 1);
            LocalDate curr = sortedDates.get(i);

            if (!prev.plusDays(1).equals(curr) && !isWeekend(prev.plusDays(1))) {
                inactivityStreak += (int) ChronoUnit.DAYS.between(prev, curr) - 1;

                if (inactivityStreak >= 3) {
                    logAnomaly(user, "inactivity streak", "No work logged for " + inactivityStreak + " days");
                }
            } else {
                inactivityStreak = 0;
            }
        }
    }

    public void checkMonthlyWorkPatterns(Map<LocalDateTime, Duration> dailyTimeWorked, UserModel user){
        List<Duration> lastMonthHours = dailyTimeWorked.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .limit(30)
                .map(Map.Entry::getValue)
                .toList();

        double monthAvg = lastMonthHours.stream()
                .mapToLong(Duration::toHours)
                .average()
                .orElse(0);

        double monthStdDev = Math.sqrt(lastMonthHours.stream()
                .mapToDouble(d -> Math.pow(d.toHours() - monthAvg, 2))
                .average()
                .orElse(0));

        for (Map.Entry<LocalDateTime, Duration> entry : dailyTimeWorked.entrySet()) {
            long hoursWorked = entry.getValue().toHours();
            double zScore = (hoursWorked - monthAvg) / (monthStdDev + 1e-6);

            if (zScore > 1) {
                logAnomaly(user, "irregular work pattern", "Worked " + hoursWorked + " hours (z-score: " + String.format("%.2f", zScore) + ")");
            }else if (zScore < -1) {
                logAnomaly(user, "underutilized", "Worked only " + hoursWorked + " hours (z-score: " + String.format("%.2f", zScore) + ")");
            }
        }
    }

    public void checkHealthyBehaviour(List<TimeLogModel> logs, UserModel user){
        Map<LocalDateTime, List<TimeLogModel>> logsByDate = logs.stream()
                                                .collect(Collectors.groupingBy(log -> log.getStartDateTime()
                                                .toLocalDate()
                                                .atStartOfDay()));

        for (Map.Entry<LocalDateTime, List<TimeLogModel>> entry : logsByDate.entrySet()) {
            List<TimeLogModel> dailyLogs = entry.getValue();

            for (TimeLogModel log : dailyLogs) {
                int startHour = log.getStartDateTime().getHour();
                if (startHour < 6 || startHour > 22) {
                    logAnomaly(user, "unusual work hours", "Logged time at " + log.getStartDateTime());
                }
            }

            for (TimeLogModel log : dailyLogs) {
                if (log.getEndDateTime() != null) {
                    long sessionHours = Duration.between(log.getStartDateTime(), log.getEndDateTime()).toHours();
                    if (sessionHours > 4) {
                        logAnomaly(user, "long continuous work session", sessionHours + " hours without a break");
                    }
                }
            }
        }
    }

    public void multiTaskingCheck(List<TimeLogModel> logs, UserModel user){
        long switchStreak = logs.stream()
                .filter(log -> log.getEndDateTime() != null)
                .map(log -> Duration.between(log.getStartDateTime(), log.getEndDateTime()).toHours())
                .filter(duration -> duration < 2.5)
                .count();

        if (switchStreak > 5) {
            logAnomaly(user, "task switching fatigue", switchStreak + " short tasks in one day");
        }
    }

    public Map<LocalDateTime, Duration> calculateDailyWorkHours(List<TimeLogModel> logs){
        return logs.stream()
                .filter(log -> log.getEndDateTime() != null)
                .collect(Collectors.groupingBy(
                        log -> log.getStartDateTime().toLocalDate().atStartOfDay(),
                        Collectors.reducing(Duration.ZERO,
                                log -> Duration.between(log.getStartDateTime(), log.getEndDateTime()),
                                Duration::plus)));
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}
