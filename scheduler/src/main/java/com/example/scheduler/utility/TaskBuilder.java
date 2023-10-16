package com.example.scheduler.utility;

import com.example.scheduler.info;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;


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
}
