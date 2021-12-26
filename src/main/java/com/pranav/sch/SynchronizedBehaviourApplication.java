package com.pranav.sch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.pranav.sch.callerJob.JobScheduler;
import com.pranav.sch.utility.MyConstants;

@SpringBootApplication
public class SynchronizedBehaviourApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SynchronizedBehaviourApplication.class);

	@Autowired
	JobScheduler schJob;

	public static void main(String[] args) {
		log.info("Main Started");
		SpringApplication.run(SynchronizedBehaviourApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String mode = args[0];
		log.info("Application started with the with mode: {}", mode);
		if (mode.equalsIgnoreCase(MyConstants.SCHEDULER_START)) {
			schJob.start();
		} else if (mode.equalsIgnoreCase(MyConstants.SCHEDULER_STOP)) {
			schJob.stop();
		}

	}

}
