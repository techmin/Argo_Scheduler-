package com.example.scheduler.timerService;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.scheduler.jobs.JobExecutor;

@Service
public class SchedulerService {
    private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);
    private final Scheduler scheduler;


    @Autowired
    public SchedulerService(Scheduler scheduler){
        this.scheduler = scheduler;
    }
}
