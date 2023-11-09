package com.example.scheduler.repositories;

import com.example.scheduler.entities.JobProperty;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface JobPropertyRep extends JpaRepository<JobProperty, Long>{
    JobProperty findByJobName(String name);
    // Optional<JobProperty> findById(Long id);
}




