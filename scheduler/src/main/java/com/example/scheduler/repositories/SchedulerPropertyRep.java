package com.example.scheduler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scheduler.entities.SchedulerProperty;

public interface SchedulerPropertyRep extends JpaRepository<SchedulerProperty, Long>{
    
}
