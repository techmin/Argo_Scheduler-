package com.example.scheduler.controller;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.scheduler.timerService.SchedulerService;
import com.example.scheduler.entities.Appointments;
// import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {
    // private static final Logger LOG = LoggerFactory.getLogger(AppointmentController.class);
    
    @Autowired
    private SchedulerService service;

    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleAppointment(@RequestBody Appointments appointment) {
        try {
            service.scheduleAppointment(appointment);
            return ResponseEntity.ok("Appointment scheduled successfully");
        } catch (SchedulerException e) {
            // Log the exception details for debugging purposes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to schedule appointment due to an error on the server.");
        }
    }

    @PostMapping("/add")
    public Appointments addAppointment(@RequestBody Appointments app){
        return service.saveAppointment(app);
    }

    @GetMapping("/list")
    public List<Appointments> findAllAppointments(){
        return service.getApp();
    }


}
