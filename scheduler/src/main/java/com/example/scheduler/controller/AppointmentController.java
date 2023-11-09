// package com.example.scheduler.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.example.scheduler.timerService.SchedulerService;
// import com.example.scheduler.entities.Appointments;
// import java.time.LocalDate;
// import java.util.List;


// @RestController
// @RequestMapping("/api/appointment")
// public class AppointmentController {
    
//     @Autowired
//     private final SchedulerService schedulerService;

//     @Autowired
//     public AppointmentController(SchedulerService schedulerService){
//         this.schedulerService = schedulerService;
//     }

//     @PostMapping("/create")
//     public List<Appointments> createAppointments(@RequestBody Appointments appointments){
//         return schedulerService.(appointments);
//     }
// }
