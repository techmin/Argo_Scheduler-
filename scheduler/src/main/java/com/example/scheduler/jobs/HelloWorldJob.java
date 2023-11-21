package com.example.scheduler.jobs;

// import org.quartz.Job;
// import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// import com.example.scheduler.info.TimerInfo;


@Component
public class HelloWorldJob implements JobInterface{
    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldJob.class);
    
    @Override
    // this method will execute when the job is triggered
    public void execute(JobExecutionContext context){
        LOG.info("Hello World!");
    }
}