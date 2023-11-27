package com.example.scheduler.controller;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.scheduler.timerService.SchedulerService;
import com.example.scheduler.entities.AppointmentRequest;
import com.example.scheduler.entities.Appointments;
import com.example.scheduler.entities.RecurrenceProperty;
import com.example.scheduler.repositories.AppointmentsRep;

// import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {
    // private static final Logger LOG = LoggerFactory.getLogger(AppointmentController.class);
    
    @Autowired
    private SchedulerService service;

    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleAppointment(@RequestBody AppointmentRequest request) {
        // System.out.println(request);
        if(request == null || request.getAppointment() == null){
            return ResponseEntity.badRequest().body("Invalid appointment data");
        }
        try {
            Appointments savedAppointment = service.saveAppointment(request.getAppointment());
            AppointmentRequest newRequest = new AppointmentRequest(savedAppointment, request.getRecurrence());
            service.scheduleAppointment(newRequest);
            return ResponseEntity.ok("Appointment added and scheduled successfully");
        } catch (SchedulerException e) {
            // Log the exception details for debugging purposes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to schedule appointment due to an error on the server.");
        }

        // service.saveAppointment(request);
    }

    @PostMapping("/add")
    public Appointments addAppointment(@RequestBody Appointments app){
        return service.saveAppointment(app);
    }

    @GetMapping("/list")
    public List<Appointments> findAllAppointments(){
        return service.getApp();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody AppointmentRequest request){
        Appointments appointment = service.getAppointmentById(id);

        appointment.setAppTitle(request.getAppointment().getAppTitle());
        appointment.setStartTime(request.getAppointment().getStartTime());
        appointment.setEndTime(request.getAppointment().getEndTime());
        appointment.setStartDate(request.getAppointment().getStartDate());
        appointment.setEndDate(request.getAppointment().getEndDate());
        appointment.setRecurrence(request.getRecurrence());

        service.saveAppointment(appointment);
        
        return ResponseEntity.ok("Appointment updated and scheduled successfully");
    }

    @GetMapping("/app/{id}")
    public Appointments findAppointmentById(@PathVariable Long id){
        return service.getAppointmentById(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id){
        return service.deleteAppointment(id);
    }
}