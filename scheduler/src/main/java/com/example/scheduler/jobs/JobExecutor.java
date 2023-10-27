package com.example.scheduler.jobs;

import org.quartz.Job;
// import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

import com.example.scheduler.info.Task;
import com.example.scheduler.Demo.TaskService;


@Component
public class JobExecutor implements Job{
    private static final Logger LOG = LoggerFactory.getLogger(JobExecutor.class);

    @Autowired
    private TaskService taskService;
    
    @Override
    // this method will execute when the job is triggered
    public void execute(JobExecutionContext context){

        //list of tasks to fetch data from database
        List<Task> tasksToExecute = taskService.fetchTasksFromDatabase();

        //execute each task
        for(Task task: tasksToExecute){
            taskService.executeTask(task);
            taskService.updateTaskInDatabase(task);
        }

        







        // // JobDataMap retrieved from the "JobExecutionContext"
        // JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        // // An instance of TimerInfo, 
        // // it fetches an object from JobDataMap using getsimplename as a key
        // TimerInfo info = (TimerInfo) jobDataMap.get(JobExecutor.class.getSimpleName());

        // // to check if it runs, suppose to retrieve data stored in its job data
        // LOG.info(info.getCallbackData());
    }
}
