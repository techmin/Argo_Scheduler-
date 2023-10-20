package com.example.scheduler;

import com.example.scheduler.utility.db_connect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SchedulerApplication {

	public static void main(String[] args) {

		db_connect.connect();

		SpringApplication.run(SchedulerApplication.class, args);

	}



}
