package com.example.scheduler.timerService;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import com.example.scheduler.info.TimerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.Trigger;
import org.quartz.JobDetail;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import com.example.scheduler.jobs.JobExecutor;
import com.example.scheduler.utility.TaskBuilder;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class SchedulerService {
    private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);
    private final Scheduler scheduler;


    // @Autowired
    public SchedulerService(Scheduler scheduler){
        this.scheduler = scheduler;
    }

    public void schedule(final Class <? extends Job> jobClass, final TimerInfo info){
        final JobDetail jobDetail = TaskBuilder.jobDetail(jobClass, info);
        final Trigger trigger = TaskBuilder.trigger(jobClass, info);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    // Starting the scheduler
    @PostConstruct
    public void initScheduler(){
        try{
            scheduler.start();
        } catch (SchedulerException e){
            LOG.error(e.getMessage(), e);
        }
    }

    @PreDestroy
    public void shutdownScheduler(){
        try{
            scheduler.shutdown();
        } catch (SchedulerException e){
            LOG.error(e.getMessage(), e);
        }
    }
}
