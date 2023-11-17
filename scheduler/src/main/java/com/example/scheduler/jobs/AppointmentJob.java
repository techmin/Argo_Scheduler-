package com.example.scheduler.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppointmentJob implements JobInterface{
    private static final Logger LOG = LoggerFactory.getLogger(AppointmentJob.class);

    @Override
    public void execute(JobExecutionContext context){
        LOG.info("Appointment Scheduled");
    }
}
