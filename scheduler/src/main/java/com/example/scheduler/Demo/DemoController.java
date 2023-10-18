package com.example.scheduler.Demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.servlet.config.annotation.EnableWebMvc;


// returns a domain object
@RestController
// maps the HTTP requests to the controller based on a specified URl
@RequestMapping("/api/scheduler")
public class DemoController {

    private DemoService service;

    public DemoController(DemoService service){
        this.service = service;
    }

    // invoked in response to POST requests to runJob() a method of DemoService
    @PostMapping("/runjob")
    public void runJob(){
        service.runJob();
    }

    @PostMapping("/runCronJob")
    public void runCronJob(){
        service.runCronJob();
    }

}
