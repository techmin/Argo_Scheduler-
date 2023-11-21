package com.example.scheduler.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "job_description")
public class JobProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long job_id;

    @Column(name = "job_name")
    private String jobName;

    // put class name of the job
    @Column(name = "taskName")
    private String taskClass; 

    @Column(name = "description")
    private String description;

    private String cronExpression;

    @OneToMany(mappedBy = "jobProperty", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    public List<SchedulerProperty> schedulerProperties = new ArrayList<>();


    public Long getJob_id() {
        return this.job_id;
    }

    public void setJob_id(Long job_id) {
        this.job_id = job_id;
    }

    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getTaskClass() {
        return this.taskClass;
    }

    public void setTaskClass(String taskClass) {
        this.taskClass = taskClass;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCronExpression() {
        return this.cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public List<SchedulerProperty> getSchedulerProperties() {
        return this.schedulerProperties;
    }

    public void setSchedulerProperties(List<SchedulerProperty> schedulerProperties) {
        this.schedulerProperties = schedulerProperties;
    }

}
