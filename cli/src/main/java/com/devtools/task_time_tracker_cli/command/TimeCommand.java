package com.devtools.task_time_tracker_cli.command;

import com.devtools.task_time_tracker_cli.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;


@ShellComponent
public class TimeCommand {

    @Autowired
    private ApiService api;

    @ShellMethod(key="time-start", value="Start tracking time for a task")
    public String startTime(int task){
        if (api.authenticate()) {
            return "You must login first.";
        }

        //Make api call here
        return "Working";
    }
}
