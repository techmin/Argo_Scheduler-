package com.example.scheduler.repositories;

import com.example.scheduler.entities.JobProperty;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface JobPropertyRep extends JpaRepository<JobProperty, Long>{
    JobProperty findByJobName(String name);
    
    @Query("SELECT jp FROM JobProperty jp JOIN jp.schedulerProperties sp WHERE sp.date BETWEEN :startDate AND :endDate")
    List<JobProperty> findBySchedulerPropertiesScheduledDateBetween(LocalDate startDate, LocalDate endDate);
}