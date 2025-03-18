package com.devtools.task_time_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaskTimeTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskTimeTrackerApplication.class, args);
	}

}
