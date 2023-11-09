package com.example.scheduler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.scheduler.entities.Appointments;


@Repository
public interface AppointmentsRep extends JpaRepository<Appointments, Long>{
}