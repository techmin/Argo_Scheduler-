package com.example.scheduler.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public interface JobInterface extends Job{
    public void execute(JobExecutionContext context);
    
}
