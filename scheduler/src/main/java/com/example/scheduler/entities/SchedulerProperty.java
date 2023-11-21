package com.example.scheduler.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;


@Entity
@Table(name = "scheduler_properties")
public class SchedulerProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate date;
    
    private String config;
    private String cronExpression;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobProperty jobProperty;

    @OneToOne
    @JoinColumn(name = "recurrence_id")
    private RecurrenceProperty recurrence;


    private String description;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getConfig() {
        return this.config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getCronExpression() {
        return this.cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public JobProperty getJobProperty() {
        return this.jobProperty;
    }

    public void setJobProperty(JobProperty jobProperty) {
        this.jobProperty = jobProperty;
    }

    public RecurrenceProperty getRecurrence() {
        return this.recurrence;
    }

    public void setRecurrence(RecurrenceProperty recurrence) {
        this.recurrence = recurrence;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
