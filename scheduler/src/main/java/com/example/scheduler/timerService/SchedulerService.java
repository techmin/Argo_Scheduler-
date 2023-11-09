package com.example.scheduler.timerService;

import com.cronutils.model.field.value.IntegerFieldValue;
import com.example.scheduler.entities.RecurrenceProperty;
import com.example.scheduler.entities.SchedulerProperty;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import com.example.scheduler.info.TimerInfo;
import com.example.scheduler.repositories.AppointmentsRep;
import com.example.scheduler.repositories.JobPropertyRep;
import com.example.scheduler.entities.JobProperty;
import com.example.scheduler.entities.Appointments;

import java.time.LocalDate;
import java.util.ArrayList;

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


import static com.cronutils.model.field.expression.FieldExpressionFactory.*;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//import javax.inject.Inject;

import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.cronutils.builder.CronBuilder;
import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.CompositeCron;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.definition.*;
import com.cronutils.model.field.CronFieldName;
import com.cronutils.model.field.expression.FieldExpression;
import com.cronutils.model.field.value.SpecialChar;
import com.cronutils.parser.CronParser;




@Service
public class SchedulerService {
    private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);
    private final Scheduler scheduler;
    private JobPropertyRep jobRepository;
    // private AppointmentsRep appRepository;

    @Autowired
    public SchedulerService(Scheduler scheduler, JobPropertyRep jobRepository){
        this.scheduler = scheduler;
        this.jobRepository = jobRepository;
        // this.appRepository = appRepository;
    }
    

    public JobProperty saveJob(JobProperty jobProperty){
        return jobRepository.save(jobProperty);
    }

    public Iterable<JobProperty> saveJobs(Iterable<JobProperty> jobProperties){
        return jobRepository.saveAll(jobProperties);
    }

    public Iterable<JobProperty> getJobs(){
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



    public void schedule(Long id, LocalDate startTime){

        // final JobDetail jobDetail = TaskBuilder.jobDetail(jobClass, info);
        // final Trigger trigger = TaskBuilder.trigger(jobClass, info);

        try {
            // scheduler a job
            // scheduler.scheduleJob(jobDetail, trigger);
            // getJobById(id);
        

            
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
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





    //setting up the cron stuff
    private Appointments cronSave(Appointments appt, SchedulerProperty schedProp) throws Exception {
        if (appt.getStartDate() == null || appt.getStartTime() == null || appt.getEndTime() == null) {
            throw new Exception("Start Date, Start time or end time is null");

        }


        LocalDateTime start = LocalDateTime.of(appt.getStartDate(), appt.getStartTime());
        LocalDateTime end;

        if ((appt.getRecurrence() != null)) {
            end = null;

        } else if (appt.getEndDate() == null) {
            throw new Exception("Please provide end date");
        } else {
            end = LocalDateTime.of(appt.getEndDate(), appt.getEndTime());

            //checking to see if the start date is after the end
            if (start.isAfter(end)) {
                throw new Exception("Start date can't be after end time");
            }
        }

        List<FieldExpression> years = new ArrayList<>();
        List<FieldExpression> months = new ArrayList<>();
        CronBuilder cronBldr = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));

        //if its a never ende appointment
        if (end == null) {
            years.add(always());
            months.add(always());
        } else
        {
            int yearLen = end.getYear() - start.getYear();

            if(yearLen < 0)
            {
                throw new Exception("end date can't be before start date");
            }

            //if same year
            if (yearLen == 0)
            {
                cronBldr.withYear(on(start.getYear()));

                if(appt.getRecurrence() != null)
                {
                    cronBldr.withMonth(on(appt.getRecurrence().getFreq()));

                }
                else
                {
                    //every month b/wn start and end date
                    cronBldr.withMonth(between(start.getMonthValue(), end.getMonthValue()));
                }

            }
            else
            {
                for(int i = 0; i <=yearLen; i++)
                {
                    years.add(on(start.getYear() + i));

                    if(i== 0)
                    {
                        months.add(between(start.getMonthValue(),12));
                    }
                    else if(i == yearLen)
                    {
                        months.add((between(1, end.getMonthValue() )));
                    }
                    else
                    {
                        months.add(always());
                    }
                }//end of for loop

            }//end of  inner else

        }//end of outer else

        //assigning the daily recurrence properties
        if(appt.getRecurrence().getDayFreq() == '*' )
        {
            cronBldr.withDoW(questionMark()).withDoM(always());
        }

        //assiging the weekly recurrence properties
        if(appt.getRecurrence().getDaysOfWeek() != null)
        {
            cronBldr.withDoM(questionMark());
            FieldExpression fe;

            List<RecurrenceProperty.daysOfWeek> daysOfWeek = appt.getRecurrence().getDaysOfWeek().stream().toList();

            if(appt.getRecurrence().getDaysOfWeek() != null)
            {
                fe = on(appt.getRecurrence().getDaysOfWeek().get(0).ordinal());

                for(int i =1; i < daysOfWeek.size();i++)
                {
                    fe = fe.and(on(daysOfWeek.get(i).ordinal()));
                }

                cronBldr.withDoM(fe); 

            }

        }

        if(appt.getRecurrence().getDayOfMonth() != 0 )
        {
            if(appt.getRecurrence().getDayOfMonth() > 30)
            {
                cronBldr.withDoM(on(SpecialChar.L)).withDoW(questionMark());
            }
            else
            {
                cronBldr.withDoM(on(appt.getRecurrence().getDayOfMonth())).withDoW(questionMark());
            }
        }
        else
        {
            if(appt.getRecurrence().getDayOfMonth() ==0)
            {
                throw new Exception("Not enough data to make a recurrence");
            }

            for(int i =0; i < appt.getRecurrence().getDaysOfWeek().size(); i++)
            {
                cronBldr.withDoM(questionMark()).withDoW(on(appt.getRecurrence().getDaysOfWeek().get(i).ordinal()));

            }

        }


        cronBldr.withHour(on(start.getHour())).withMinute(on(start.getMinute())).withSecond(on(start.getSecond()));

        String cronAsString = "";
        List<Cron> cronExp = new ArrayList<>();
        Cron cron;

        CompositeCron compositeCron;

        ///creating descirpor for a specific locale
        CronDescriptor descriptor = CronDescriptor.instance();


        if(years.size() ==0)
        {
            cron = cronBldr.instance();
            cronAsString += cron.asString();

            schedProp.setCronExpression(cronAsString);
            schedProp.setDescription(descriptor.describe(cron));

        }
        else {
            for(int i = 0; i < years.size(); i++)
            {
                cronBldr.withYear(years.get(i));

                cron = cronBldr.instance();
                cronExp.add(cron);

                cronAsString = cronExp.stream().map(Cron::asString).collect(Collectors.joining("||"));
            }

            compositeCron = new CompositeCron(cronExp);

            schedProp.setCronExpression(compositeCron.asString());
            schedProp.setDescription(cronExp.stream().map(descriptor::describe).collect(Collectors.joining("||")));


        }

        return appt;
    }
}