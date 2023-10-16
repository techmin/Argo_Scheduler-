package com.example.scheduler.Demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@RequestMapping("/api/scheduler")
public class DemoController {

    private DemoService service;

    public DemoController(DemoService service){
        this.service = service;
    }

    @PostMapping("/runjob")
    public void runJob(){
        service.runJob();
    }
}
