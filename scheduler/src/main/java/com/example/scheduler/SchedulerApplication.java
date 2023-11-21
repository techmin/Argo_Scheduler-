package com.example.scheduler;

import com.cronutils.model.Cron;
import com.example.scheduler.utility.db_connect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class SchedulerApplication {

	public static void main(String[] args) throws SQLException {

		db_connect.connect();
		db_connect.getJobs();
		String cron  = db_connect.getSchedProp(db_connect.getJobs());

		System.out.println(cron);

		SpringApplication.run(SchedulerApplication.class, args);

	}



}
