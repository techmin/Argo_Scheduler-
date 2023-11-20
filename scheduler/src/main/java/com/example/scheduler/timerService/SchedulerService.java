package com.example.scheduler.timerService;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import com.example.scheduler.info.TimerInfo;
import com.example.scheduler.jobs.AppointmentJob;
import com.example.scheduler.jobs.HelloWorldJob;
import com.example.scheduler.jobs.JobInterface;
import com.example.scheduler.repositories.AppointmentsRep;
import com.example.scheduler.repositories.JobPropertyRep;
import com.example.scheduler.repositories.SchedulerPropertyRep;
import com.example.scheduler.repositories.RecurrenceRep;
import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronNicknames;
import com.cronutils.parser.CronParser;
import com.example.scheduler.entities.JobProperty;
import com.example.scheduler.entities.RecurrenceProperty;
import com.example.scheduler.entities.Appointments;
import com.example.scheduler.entities.SchedulerProperty;

import java.util.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
// import org.hibernate.mapping.List;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
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
    private AppointmentsRep appRepository;
    private RecurrenceRep recurrenceRepository;

    @Autowired
    public SchedulerService(Scheduler scheduler, JobPropertyRep jobRepository, SchedulerPropertyRep scheduleRepository, AppointmentsRep appRepository, RecurrenceRep recurrenceRepository){
        this.scheduler = scheduler;
        this.jobRepository = jobRepository;
        this.scheduleRepository = scheduleRepository;
        this.appRepository = appRepository;
        this.recurrenceRepository = recurrenceRepository;
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
        return "Job id #" + id + " deleted!";
    }

    public JobProperty updateJob(Long id, JobProperty jobProperty){
        JobProperty existingJob = jobRepository.findById(id).orElse(null);
        existingJob.setJobName(jobProperty.getJobName());
        existingJob.setTaskClass(jobProperty.getTaskClass());
        existingJob.setCronExpression(jobProperty.getCronExpression());
        existingJob.setDescription(jobProperty.getDescription());
        return jobRepository.save(existingJob);
    }

    public void scheduleJob(Long id) throws SchedulerException, ClassNotFoundException{
        JobProperty job = jobRepository.findById(id).orElse(null);
        if(job == null){return;}

        @SuppressWarnings("unchecked")
        Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(job.getTaskClass());
        if((!CronExpression.isValidExpression(job.getCronExpression()))){
            throw new SchedulerException("Invalid cron expression: " + job.getCronExpression());}

        // for (SchedulerProperty schedulerProperty : job.getSchedulerProperties()){
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                                        .withIdentity(job.getJobName(), "group1")
                                        .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                                        .withIdentity(job.getJobName(), "group1")
                                        .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression()))
                                        .startNow()
                                        .build();

        scheduler.scheduleJob(jobDetail, trigger);
        // }
    }

    public void scheduleAppointment(Appointments data) throws SchedulerException{
        LocalDateTime startDateTime = LocalDateTime.of(data.getStartDate(), data.getStartTime());
        Date start = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());

        JobDetail jobDetail = JobBuilder.newJob(AppointmentJob.class)
                                        .withIdentity(data.getAppTitle(), "appointments")
                                        .usingJobData("appointmentId", data.getId())
                                        .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                                        .withIdentity(data.getAppTitle() + "Trigger")
                                        .startAt(start)
                                        .build();

        scheduler.scheduleJob(jobDetail, trigger);

        List<Appointments> newAppointments = new ArrayList<>();
        LocalDate currentDate = data.getStartDate();
        LocalDate enforceEndDate = (data.getEndDate() == null) ? startDateTime.plusDays(1).toLocalDate() : data.getEndDate();

        while (currentDate.isBefore(enforceEndDate)) {
            Appointments newAppointmentDTO = new Appointments();
            newAppointmentDTO.setAppTitle(data.getAppTitle());
            newAppointmentDTO.setStartDate(currentDate);
            newAppointmentDTO.setStartTime(data.getStartTime());
            newAppointmentDTO.setEndTime(data.getEndTime());
            // newAppointmentDTO.setRecurrence(recurrence);

            newAppointments.add(newAppointmentDTO);
            currentDate = currentDate.plusDays(1);
        }

        saveAllAppointments(newAppointments);
    }

    public Iterable<Appointments> saveAllAppointments(Iterable<Appointments> app){
        return appRepository.saveAll(app);
    }

    public Appointments saveAppointment(Appointments app){
        return appRepository.save(app);
    }

    public List<Appointments> getApp(){
        return appRepository.findAll();
    }









    // public List<LocalDateTime> buildSchedule(Long id, LocalDate startTime){

    //     // final JobDetail jobDetail = TaskBuilder.jobDetail(jobClass, info);
    //     // final Trigger trigger = TaskBuilder.trigger(jobClass, info);

    //     try {
    //         List<LocalDateTime> schedule = new ArrayList<>();
    //         JobProperty job = jobRepository.findById(id).orElse(null);
    //         for (SchedulerProperty scheduler : job.getSchedulerProperties()){
    //             // parse cronExpression to generate the schedule dates and times
    //             Cron cron = (Cron) TaskBuilder.cronTrigger(scheduler, job);
    //             LocalDateTime scheduleStart = LocalDateTime.of(startTime, LocalTime.now());
                
    //             createSchedule(schedule, cron, scheduleStart, scheduleStart, job);

    //             // add if statement to check repeat status if possible
    //             List<LocalDateTime> filteredSchedule = new ArrayList<>();
    //             RecurrenceProperty recurrence = scheduler.getRecurrence();
    //             LocalDate start = scheduler.getDate();
    //             Integer frequency = recurrence.getFrequency();
    //             if(frequency > 1){
    //                 switch (recurrence.getRepeat()) {
    //                 case DAILY:
    //                     int index = -1;
    //                     LocalDate dt = start;
    //                     while(true){
    //                         LocalDate finalDate = dt;
    //                         for(int i = 0; i < schedule.size(); i++){
    //                             if(finalDate.equals(schedule.get(i).toLocalDate())){
    //                                 index = i;
    //                                 break;
    //                             }
    //                         }
    //                         if (index != -1){
    //                             break;
    //                         }
    //                     }
    //                 if(index != -1){
    //                     int rem = index % frequency;
    //                     filteredSchedule = IntStream.range(rem, schedule.size())
    //                                             .filter(n -> n % frequency == rem)
    //                                             .mapToObj(schedule::get) 
    //                                             .collect(Collectors.toList());     
    //                 }
    //                 break;                  
                        
                        
    //                 case WEEKLY:
    //                     if(!schedule.isEmpty()){
    //                         int rem = start.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) % frequency;
    //                         filteredSchedule = schedule.stream()
    //                                                 .filter(s -> (s.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) % frequency) == rem)
    //                                                 .collect(Collectors.toList());
    //                     }
    //                     break;

    //                 case MONTHLY:
    //                     if(!schedule.isEmpty()){
    //                         int rem = start.getMonthValue() % frequency;
    //                         filteredSchedule = schedule.stream()
    //                                                 .filter(s -> (s.getMonthValue() % frequency) == rem)
    //                                                 .collect(Collectors.toList());
    //                     }
    //                     break;
                
    //                 default:
    //                     filteredSchedule = Collections.emptyList();
    //                     break;
    //                 }
    //             }
    //             return filteredSchedule;
                
    //         }

    //         return schedule;
    //     } catch (Exception e) {
    //         LOG.error(e.getMessage(), e);
    //         return new ArrayList<>();
    //     }
    // }

    // private void createSchedule(List<LocalDateTime> schedule, Cron cron, LocalDateTime scheduleStart, LocalDateTime endOn, JobProperty job){
    //     try {
    //         CronExpression ce = new CronExpression(cron.asString());
    //         Date nextValidTime = Date.from(scheduleStart.atZone(ZoneId.systemDefault()).toInstant());
    //         while (!scheduleStart.isAfter(endOn)) {
    //             nextValidTime = ce.getNextValidTimeAfter(nextValidTime);
    //             if (nextValidTime == null || nextValidTime.toInstant().isAfter(endOn.atZone(ZoneId.systemDefault()).toInstant())) {
    //                 break;
    //             }
    //             LOG.info("next: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nextValidTime));
    //             LocalDateTime nextValidDateTime = LocalDateTime.ofInstant(nextValidTime.toInstant(), ZoneId.systemDefault());
    //             schedule.add(nextValidDateTime);
    //             scheduleStart = nextValidDateTime;
    //     }
    //     } catch (Exception e) {
    //         LOG.error(e.getMessage(), e);
    //     }
    // }

    // public Appointments saveAppointment(Appointments app){
    //     RecurrenceProperty recurrence = app.getRecurrence();
    //     if(recurrence != null && recurrence.getId() == null){
    //         recurrence = recurrenceRepository.save(recurrence);
    //         app.setRecurrence(recurrence);
    //     }
    //     return appRepository.save(app);
    // }

    

    // public List<Appointments> getAllAppointments(){
    //     return appRepository.findAll();
    // }

    // public List<Appointments> createAppointments(Appointments app){
    //     List<Appointments> newAppointments = new ArrayList<>();
    //     RecurrenceProperty recurrence = app.getRecurrence();

    //     LocalDate currentDate = app.getStartDate();
    //     if (currentDate == null){
    //         throw new IllegalArgumentException("You must enter a start date!");
    //     }
    //     LocalDate enforceEndDate = (app.getEndDate() == null) ? currentDate.plusDays(1) : app.getEndDate();

    //     if(recurrence == null){
    //         saveAppointment(app);
    //         newAppointments.add(app);
    //         return newAppointments;
    //     } 
    //     while (currentDate.isBefore(enforceEndDate)) {
    //         Appointments newAppointmentDTO = new Appointments();
    //         newAppointmentDTO.setAppTitle(app.getAppTitle());
    //         newAppointmentDTO.setStartDate(currentDate);
    //         newAppointmentDTO.setStartTime(app.getStartTime());
    //         newAppointmentDTO.setEndTime(app.getEndTime());
    //         newAppointmentDTO.setRecurrence(recurrence);

    //         newAppointments.add(newAppointmentDTO);
    //         currentDate = currentDate.plusDays(recurrence.getIntervalAmount());
    //     }
    //     saveAllAppointments(newAppointments);
    //     return newAppointments;
    // }


    
    @PostConstruct
    // Starting the scheduler
    public void initScheduler(){
        try{
            scheduler.start();

            List<JobProperty> jobs = jobRepository.findAll();
            for(JobProperty job : jobs){
                try {
                    scheduleJob(job.getJob_id());
                } catch (Exception e) {
                    LOG.error("Error scheduling job: " + job.getJobName(), e);
                }
            }
            
        } catch (SchedulerException e){
            LOG.error("Error starting the scheduler", e);
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