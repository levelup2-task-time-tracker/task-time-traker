package com.devtools.task_time_tracker_cli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskTimeTrackerCliApplication {

	public static void main(String[] args) {

		String logo = """
					████████  █████  ███████ ██   ██     ████████ ██ ███    ███ ███████     ████████ ██████   █████   ██████ ██   ██ ███████ ██████
					   ██    ██   ██ ██      ██  ██         ██    ██ ████  ████ ██             ██    ██   ██ ██   ██ ██      ██  ██  ██      ██   ██
					   ██    ███████ ███████ █████          ██    ██ ██ ████ ██ █████          ██    ██████  ███████ ██      █████   █████   ██████
					   ██    ██   ██      ██ ██  ██         ██    ██ ██  ██  ██ ██             ██    ██   ██ ██   ██ ██      ██  ██  ██      ██   ██
					   ██    ██   ██ ███████ ██   ██        ██    ██ ██      ██ ███████        ██    ██   ██ ██   ██  ██████ ██   ██ ███████ ██   ██
				""";

		System.out.println(logo);
		SpringApplication.run(TaskTimeTrackerCliApplication.class, args);
	}

}
