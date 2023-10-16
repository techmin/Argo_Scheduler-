package com.example.scheduler.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class JobExecutor implements Job{
    private static final Logger LOG = LoggerFactory.getLogger(JobExecutor.class);
    
    @Override
    public void execute(JobExecutionContext context){
        LOG.info("Job Scheduled");
    }
}
