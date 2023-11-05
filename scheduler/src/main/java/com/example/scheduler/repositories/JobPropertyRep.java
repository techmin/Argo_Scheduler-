package com.example.scheduler.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.scheduler.entities.JobProperty;


@Repository
public interface JobPropertyRep extends CrudRepository<JobProperty, Long>{
}
