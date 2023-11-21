package com.example.scheduler.jobs;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.scheduler.info.TimerInfo;


@Component
//define own interface, JobInterface
public class HelloWorldJob extends JobInterface {
    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldJob.class);
    
    // this method will execute when the job is triggered
    public void execute(JobExecutionContext context){
        // JobDataMap retrieved from the "JobExecutionContext

        // to check if it runs, suppose to retrieve data stored in its job data
        LOG.info("Hello World");
    }
}