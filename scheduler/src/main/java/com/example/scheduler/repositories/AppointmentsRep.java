package com.example.scheduler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.scheduler.entities.Appointments;

public interface AppointmentsRep extends JpaRepository<Appointments, Long>{
}
