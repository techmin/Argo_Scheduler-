package com.example.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PutMapping("/update")
    public JobProperty updateJob(@RequestBody JobProperty jobProperty){
        return service.updateJob(jobProperty);
    }

    @DeleteMapping("/delete/id/{id}")
    public String deleteJob(@PathVariable Long id){
        return service.deleteJob(id);
    }
}
