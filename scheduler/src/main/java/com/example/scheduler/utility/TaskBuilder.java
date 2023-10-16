package com.example.scheduler.utility;

import com.example.scheduler.info.TimerInfo;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
// import java.util.Date;


public final class TaskBuilder {
    private TaskBuilder() {}

    public static JobDetail jobDetail(final Class<? extends Job> jobClass, final TimerInfo info){
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(jobClass.getSimpleName(), info);

        return JobBuilder.newJob(jobClass)
                        .withIdentity(jobClass.getSimpleName())
                        .setJobData(jobDataMap)
                        .build();
    }

    public static Trigger trigger(final Class<? extends Job> jobClass, final TimerInfo info){
        SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
                                        .withIntervalInSeconds(info.getRepeatIntervalS());

        if (info.isRunForever()){
            builder = builder.repeatForever();
        } else {
            builder = builder.withRepeatCount(info.getTotalFireCount() - 1);
        }

        return TriggerBuilder.newTrigger()
                    .withIdentity(jobClass.getSimpleName())
                    .withSchedule(builder)
                    // .startAt(new Date(System.currentTimeMillis() + info.getInitialOffsetS()))
                    .build();
    }
}


