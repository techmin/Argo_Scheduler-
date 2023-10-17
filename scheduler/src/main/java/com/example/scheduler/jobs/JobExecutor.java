package com.example.scheduler.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.scheduler.info.TimerInfo;


@Component
public class JobExecutor implements Job{
    private static final Logger LOG = LoggerFactory.getLogger(JobExecutor.class);
    
    @Override
    // this method will execute when the job is triggered
    public void execute(JobExecutionContext context){
        // JobDataMap retrieved from the "JobExecutionContext"
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        // An instance of TimerInfo, 
        // it fetches an object from JobDataMap using getsimplename as a key
        TimerInfo info = (TimerInfo) jobDataMap.get(JobExecutor.class.getSimpleName());

        // to check if it runs, suppose to retrieve data stored in its job data
        LOG.info(info.getCallbackData());
    }
}
