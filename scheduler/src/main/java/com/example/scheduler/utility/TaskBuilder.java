package com.example.scheduler.utility;

import com.example.scheduler.info.TimerInfo;
// import com.example.scheduler.timerService.SchedulerService;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
// import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class TaskBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(TaskBuilder.class);

    private TaskBuilder() {}

    // jobDetail method
    public static JobDetail jobDetail(final Class<? extends Job> jobClass, final TimerInfo info){
        // store configuration data for job 
        final JobDataMap jobDataMap = new JobDataMap();

        // put TimerInfo object using name as a key
        jobDataMap.put(jobClass.getSimpleName(), info);

        // build and return a new job detail object
        return JobBuilder.newJob(jobClass)
                        .withIdentity(jobClass.getSimpleName()) //for now, using default group
                        .setJobData(jobDataMap) //replace the jobDetail's JobDataMap
                        .build();
    }

    // trigger method
    public static Trigger trigger(final Class<? extends Job> jobClass, final TimerInfo info){
        // sets up a simple schedule based on repeat interval
        SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
                                        .withIntervalInSeconds(info.getRepeatIntervalS());

        // sets up the job to run forever   
        if (info.isRunForever()){
            builder = builder.repeatForever();
        } else {
            // sets up the # of times the job should run
            builder = builder.withRepeatCount(info.getTotalFireCount() - 1);
        }

        // build and return a new trigger, determines the timing and frequency of the job's
        return TriggerBuilder.newTrigger()
                    .withIdentity(jobClass.getSimpleName())
                    .withSchedule(builder) //will define the Trigger's schedule
                    // .startAt(new Date(System.currentTimeMillis() + info.getInitialOffsetS()))
                    .build();
    }

    // new
    public static Trigger cronTrigger(final Class<? extends Job> jobClass, final TimerInfo info){
        
        if(info.getCronExpression() == null || info.getCronExpression().trim().isEmpty()){
            LOG.error("Cron expression must be provided!");
            return null;
        }
        
        return TriggerBuilder.newTrigger()
                            .withIdentity(jobClass.getSimpleName())
                            .withSchedule(CronScheduleBuilder.cronSchedule(info.getCronExpression()))
                            .build();
    }
}