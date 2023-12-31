package com.example.scheduler.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.engine.internal.Cascade;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;




@Entity
@Table(name = "appointments")
public class Appointments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title")
    private String appTitle;

    @NonNull
    private LocalDate startDate;
    private LocalDate endDate;

    @NonNull
    private LocalTime startTime;
    private LocalTime endTime;

    // @ManyToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "recurrence_id")
    // private RecurrenceProperty recurrence;

    // @ManyToOne
    // @JoinColumn(name = "job_id")
    // private JobProperty jobProperty;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppTitle() {
        return this.appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    // public RecurrenceProperty getRecurrence() {
    //     return this.recurrence;
    // }

    // public void setRecurrence(RecurrenceProperty recurrence) {
    //     this.recurrence = recurrence;
    // }

    // public JobProperty getJobProperty() {
    //     return this.jobProperty;
    // }

    // public void setJobProperty(JobProperty jobProperty) {
    //     this.jobProperty = jobProperty;
    // }
}
