package com.example.scheduler.Demo;

import org.springframework.stereotype.Service;

import com.example.scheduler.info.TimerInfo;
import com.example.scheduler.jobs.JobExecutor;
import com.example.scheduler.timerService.SchedulerService;

@Service
public class DemoService {
    private final SchedulerService scheduler;

    public DemoService(final SchedulerService scheduler){
        this.scheduler = scheduler;
    }

    public void runJob(){
        final TimerInfo info = new TimerInfo();
        info.setTotalFireCount(5);
        info.setRepeatIntervalS(2);
        info.setInitialOffsetS(1);
        info.setCallbackData("My Callback Data");


        scheduler.schedule(JobExecutor.class, info);
    }
}