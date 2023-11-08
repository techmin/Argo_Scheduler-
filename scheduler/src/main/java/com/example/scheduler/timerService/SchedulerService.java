package com.example.scheduler.timerService;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import com.example.scheduler.info.TimerInfo;
// import com.example.scheduler.repositories.AppointmentsRep;
import com.example.scheduler.repositories.JobPropertyRep;
import com.example.scheduler.repositories.SchedulerPropertyRep;
import com.example.scheduler.entities.JobProperty;
// import com.example.scheduler.entities.Appointments;
import com.example.scheduler.entities.SchedulerProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.hibernate.mapping.List;
import org.quartz.Job;
import org.quartz.Trigger;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.scheduler.utility.TaskBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;



@Service
public class SchedulerService {
    private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);
    private final Scheduler scheduler;
    private JobPropertyRep jobRepository;
    SchedulerPropertyRep scheduleRepository;
    // private AppointmentsRep appRepository;

    @Autowired
    public SchedulerService(Scheduler scheduler, JobPropertyRep jobRepository, SchedulerPropertyRep scheduleRepository){
        this.scheduler = scheduler;
        this.jobRepository = jobRepository;
        this.scheduleRepository = scheduleRepository;
        // this.appRepository = appRepository;
    }
    

    public JobProperty saveJob(JobProperty jobProperty){
        return jobRepository.save(jobProperty);
    }

    public Iterable<JobProperty> saveJobs(Iterable<JobProperty> jobProperties){
        return jobRepository.saveAll(jobProperties);
    }

    public List<JobProperty> getJobs(){
        return jobRepository.findAll();
    }

    public JobProperty getJobById(Long id){
        return jobRepository.findById(id).orElse(null);
    }

    public JobProperty getJobByName(String name){
        return jobRepository.findByJobName(name);
    }

    public String deleteJob(Long id){
        jobRepository.deleteById(id);
        return "Job + " + id + " deleted!";
    }

    public JobProperty updateJob(JobProperty jobProperty){
        JobProperty existingJob = jobRepository.findById(jobProperty.getJob_id()).orElse(null);
        existingJob.setJobName(jobProperty.getJobName());
        existingJob.setTaskName(jobProperty.getTaskName());
        existingJob.setDescription(jobProperty.getDescription());
        return jobRepository.save(existingJob);
    }


    // public Iterable<Appointments> createAppointments(LocalDate startDate){
    //     Iterable<Appointments> newAppointment = new ArrayList<>();

    //     return newAppointment;
    // }



    public void buildSchedule(Long id, LocalDate startTime){

        // final JobDetail jobDetail = TaskBuilder.jobDetail(jobClass, info);
        // final Trigger trigger = TaskBuilder.trigger(jobClass, info);

        try {
            // scheduler a job
            // scheduler.scheduleJob(jobDetail, trigger);
            // List<JobProperty> jobs = getJobs();

            // for (JobProperty job: jobs){
            //     List<SchedulerProperty> schedulerProperties = job.getSchedulerProperties();
            //     for (SchedulerProperty schedulerProperty : schedulerProperties){
            //         getSchedule(job, schedulerProperty);
            //     }
            // }
            // List<LocalDateTime> schedule = new ArrayList<>();
            // Integer frequency = 
        

            
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void getSchedule(JobProperty job, SchedulerProperty schedulerProperty){

    }

    
    @PostConstruct
    // Starting the scheduler
    public void initScheduler(){
        try{
            scheduler.start();
        } catch (SchedulerException e){
            LOG.error(e.getMessage(), e);
        }
    }

    // ??
    @PreDestroy
    public void shutdownScheduler(){
        try{
            scheduler.shutdown();
        } catch (SchedulerException e){
            LOG.error(e.getMessage(), e);
        }
    }
}