package com.example.scheduler.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.scheduler.entities.SchedulerProperty;

@Repository
public interface SchedulerPropertyRep extends CrudRepository<SchedulerProperty, Long>{
}
