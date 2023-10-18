package com.example.scheduler.Demo;

import org.springframework.stereotype.Service;

import com.example.scheduler.info.TimerInfo;
import com.example.scheduler.jobs.JobExecutor;
import com.example.scheduler.timerService.SchedulerService;
import com.example.scheduler.utility.TaskBuilder;
import org.quartz.CronTrigger;


@Service
// spring service than can be injected into other components
public class DemoService {
    // reference to the scheduler service, will be used to scheduler jobs
    private final SchedulerService scheduler;

    // spring will provide the required bean when creating an instance
    public DemoService(final SchedulerService scheduler){
        this.scheduler = scheduler;
    }

    public void runJob(){
        final TimerInfo info = new TimerInfo();
        // job will run a total 5 times
        info.setTotalFireCount(5);

        // job will repeat every 2 seconds
        info.setRepeatIntervalS(2);

        // job will start after a delay of 1
        info.setInitialOffsetS(1);

        // used to test if the job is being executed
        info.setCallbackData("Scheduling...");

        // schedules the jobExecutor job with given info
        // scheduler.schedule(JobExecutor.class, info);
    }


    // // new
    public void runCronJob(){

        final TimerInfo info = new TimerInfo();
        info.setCronExpression("0 * * * * ?");
        info.setCallbackData("Executing cron job");

        CronTrigger trigger = (CronTrigger) TaskBuilder.cronTrigger(JobExecutor.class, info);
        if(trigger != null){
            scheduler.schedule(JobExecutor.class, info, trigger);
        }
    }
} 