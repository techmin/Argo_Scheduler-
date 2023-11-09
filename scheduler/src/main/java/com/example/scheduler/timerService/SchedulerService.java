package com.example.scheduler.timerService;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import com.example.scheduler.info.TimerInfo;
// import com.example.scheduler.repositories.AppointmentsRep;
import com.example.scheduler.repositories.JobPropertyRep;
import com.example.scheduler.repositories.SchedulerPropertyRep;
import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronNicknames;
import com.cronutils.parser.CronParser;
import com.example.scheduler.entities.JobProperty;
import com.example.scheduler.entities.RecurrenceProperty;
// import com.example.scheduler.entities.Appointments;
import com.example.scheduler.entities.SchedulerProperty;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.CronExpression;
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



    public List<LocalDateTime> buildSchedule(Long id, LocalDate startTime){

        // final JobDetail jobDetail = TaskBuilder.jobDetail(jobClass, info);
        // final Trigger trigger = TaskBuilder.trigger(jobClass, info);

        try {
            List<LocalDateTime> schedule = new ArrayList<>();
            JobProperty job = jobRepository.findById(id).orElse(null);
            for (SchedulerProperty scheduler : job.getSchedulerProperties()){
                // parse cronExpression to generate the schedule dates and times
                Cron cron = (Cron) TaskBuilder.cronTrigger(scheduler, job);
                LocalDateTime scheduleStart = LocalDateTime.of(startTime, LocalTime.now());
                
                createSchedule(schedule, cron, scheduleStart, scheduleStart, job);

                // add if statement to check repeat status if possible
                List<LocalDateTime> filteredSchedule = new ArrayList<>();
                RecurrenceProperty recurrence = scheduler.getRecurrence();
                LocalDate start = scheduler.getDate();
                Integer frequency = recurrence.getFrequency();
                if(frequency > 1){
                    switch (recurrence.getRepeat()) {
                    case DAILY:
                        int index = -1;
                        LocalDate dt = start;
                        while(true){
                            LocalDate finalDate = dt;
                            for(int i = 0; i < schedule.size(); i++){
                                if(finalDate.equals(schedule.get(i).toLocalDate())){
                                    index = i;
                                    break;
                                }
                            }
                            if (index != -1){
                                break;
                            }
                        }
                    int rem = index % frequency;
                    filteredSchedule = IntStream.range(rem, schedule.size())
                                                .filter(n -> n % frequency == rem)
                                                .mapToObj(schedule::get) 
                                                .toList();                       
                        
                        
                    case WEEKLY:
                        break;

                    case MONTHLY:
                        break;
                
                    default:
                        break;
                    }
                }
                return filteredSchedule;
                
            }

            return schedule;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private void createSchedule(List<LocalDateTime> schedule, Cron cron, LocalDateTime scheduleStart, LocalDateTime endOn, JobProperty job){
        try {
            CronExpression ce = new CronExpression(cron.asString());
            Date nextValidTime = Date.from(scheduleStart.atZone(ZoneId.systemDefault()).toInstant());
            while (!scheduleStart.isAfter(endOn)) {
                nextValidTime = ce.getNextValidTimeAfter(nextValidTime);
                if (nextValidTime == null || nextValidTime.toInstant().isAfter(endOn.atZone(ZoneId.systemDefault()).toInstant())) {
                    break;
                }
                LOG.info("next: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nextValidTime));
                LocalDateTime nextValidDateTime = LocalDateTime.ofInstant(nextValidTime.toInstant(), ZoneId.systemDefault());
                schedule.add(nextValidDateTime);
                scheduleStart = nextValidDateTime;
        }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }


      // public Iterable<Appointments> createAppointments(LocalDate startDate){
    //     Iterable<Appointments> newAppointment = new ArrayList<>();

    //     return newAppointment;
    // }


    
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