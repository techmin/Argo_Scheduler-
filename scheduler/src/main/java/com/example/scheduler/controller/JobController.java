package com.example.scheduler.controller;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.scheduler.entities.JobProperty;
import com.example.scheduler.timerService.SchedulerService;



@RestController
@RequestMapping("/api/scheduler")
public class JobController {

    private static final Logger LOG = LoggerFactory.getLogger(JobController.class);
    
    @Autowired
    private SchedulerService service;

    @PostMapping("/addJob")
    public JobProperty addJob(@RequestBody JobProperty jobProperty){
        return service.saveJob(jobProperty);
    }

    @PostMapping("/addJobs")
    public Iterable<JobProperty> addJobs(@RequestBody Iterable<JobProperty> jobProperties){
        return service.saveJobs(jobProperties);
    }

    @GetMapping("/jobs")
    public Iterable<JobProperty> findAllJobs(){
        return service.getJobs();
    }

    @GetMapping("/job/id/{id}")
    public JobProperty findJobById(@PathVariable Long id){
        return service.getJobById(id);
    }

    @GetMapping("/job/name/{name}")
    public JobProperty findJobByName(@PathVariable String name){
        return service.getJobByName(name);
    }

    @PutMapping("/update/{id}")
    public JobProperty updateJob(@PathVariable Long id, @RequestBody JobProperty jobProperty){
        return service.updateJob(id, jobProperty);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id){
        return service.deleteJob(id);
    }

    @PostMapping("/schedule/{id}")
    public ResponseEntity<String> scheduleJob(@PathVariable Long id){
        try {
            service.scheduleJob(id);
            return ResponseEntity.ok("Job scheduled successfully");
        } catch (SchedulerException e) {
            LOG.error("Error scheduling job: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error scheduling job!");
        } catch (ClassNotFoundException e){
            LOG.error("Job class not found: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Job class not found!");
        }
    }
}
