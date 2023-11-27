package com.example.scheduler.timerService;

import org.quartz.Scheduler;
import org.quartz.CronScheduleBuilder;
import org.quartz.SchedulerException;
import com.example.scheduler.jobs.AppointmentJob;
// import com.example.scheduler.jobs.HelloWorldJob;
// import com.example.scheduler.jobs.JobInterface;
import com.example.scheduler.repositories.AppointmentsRep;
import com.example.scheduler.repositories.JobPropertyRep;
import com.example.scheduler.repositories.SchedulerPropertyRep;
import com.example.scheduler.repositories.RecurrenceRep;
import com.example.scheduler.entities.JobProperty;
import com.example.scheduler.entities.RecurrenceProperty;
import com.example.scheduler.entities.Appointments.AppointmentStatus;
import com.example.scheduler.entities.AppointmentRequest;
import com.example.scheduler.entities.Appointments;
// import com.example.scheduler.entities.SchedulerProperty;

import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.CronExpression;
// import org.quartz.CronScheduleBuilder;
// import org.hibernate.mapping.List;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;



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
    }

    public Appointments getAppointmentById(Long id){
        return appRepository.findById(id).orElse(null);
    }

    public String deleteAppointment(Long id){
        appRepository.deleteById(id);
        return "Job id#" + id + " successfully deleted";
    }

    private String createCronExpression(RecurrenceProperty.Repeat repeat, LocalDateTime startDateTime, int intervalAmount) {
        int minute = startDateTime.getMinute();
        int hour = startDateTime.getHour();
        int dayOfMonth = startDateTime.getDayOfMonth();
        int month = startDateTime.getMonthValue();
    
        StringBuilder cronExpression = new StringBuilder("0 "); // seconds
        cronExpression.append(minute).append(" ");
        cronExpression.append(hour).append(" ");
    
        switch (repeat) {
            case DAILY:
                cronExpression.append("* * ?");
                break;
            case WEEKLY:
                cronExpression.append("? * ").append(startDateTime.getDayOfWeek().getValue());
                break;
            case MONTHLY:
                cronExpression.append(dayOfMonth).append(" * ?");
                break;
            case YEARLY:
                cronExpression.append(dayOfMonth).append(" ").append(month).append(" ?");
                break;
            default:
                throw new IllegalArgumentException("Unsupported repeat type");
        }
    
        // If intervalAmount is greater than 1, cron expression accordingly should be adjusted accordingly.
           
        return cronExpression.toString();
    }
    

    @Transactional
    public void scheduleAppointment(AppointmentRequest data) throws SchedulerException{
        LocalDateTime startDateTime = LocalDateTime.of(data.getAppointment().getStartDate(), data.getAppointment().getStartTime());
        Date start = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());

        JobDetail jobDetail = JobBuilder.newJob(AppointmentJob.class)
                                        .withIdentity(data.getAppointment().getAppTitle(), "appointments")
                                        .usingJobData("appointmentId", data.getAppointment().getId())
                                        .build();

        Trigger trigger;

        if(data.getRecurrence() == null){
            trigger = TriggerBuilder.newTrigger()
                                .withIdentity(data.getAppointment().getAppTitle() + "Trigger")
                                .startAt(start)
                                .build();
        } else {

            String cronExpression = createCronExpression(data.getRecurrence().getRepeat(), startDateTime, data.getRecurrence().getIntervalAmount());
            trigger = TriggerBuilder.newTrigger()
                                    .withIdentity(data.getAppointment().getAppTitle(),"Trigger")
                                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                                    .startAt(start)
                                    .build();
        }
        
        Appointments appointment = data.getAppointment();
        appointment.setStatus(AppointmentStatus.WAITING);
        appRepository.save(appointment);

        try {
            appointment.setStatus(AppointmentStatus.SCHEDULED);
            scheduler.scheduleJob(jobDetail, trigger);
            appRepository.save(appointment);
        } catch (SchedulerException e) {
            LOG.error("Failed to schedule appointment: {}", e.getMessage());
        }
        
    }

    public Iterable<Appointments> saveAllAppointments(Iterable<Appointments> app){
        return appRepository.saveAll(app);
    }

    public Appointments saveAppointment(Appointments app){
        if(app.getStatus() == null){
            app.setStatus(AppointmentStatus.WAITING);
        }
        return appRepository.save(app);
    }

    public List<Appointments> getApp(){
        return appRepository.findAll();
    }
    
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