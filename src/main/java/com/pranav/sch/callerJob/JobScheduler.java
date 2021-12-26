package com.pranav.sch.callerJob;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.pranav.sch.ChildJobs.Starter;

@Component
public class JobScheduler {

	private static final Logger log = LoggerFactory.getLogger(JobScheduler.class);

	@Value("${scheduler.switch}")
	private String control;

	@Value("${run.timeDuration}")
	private String interval;

	@Autowired
	private SchedulerFactoryBean schBean;

	public boolean start() throws SchedulerException {
		boolean status = false;
		log.info("Service is about to start");

		Scheduler scheduler = schBean.getScheduler();
		scheduler.start();

		if ("ON".equalsIgnoreCase(control)) {
			log.info("Jobs started for my switch");

			try {
				JobDetail job = JobBuilder.newJob(Starter.class).withIdentity("JobScheduler","JobSchedulerGroup").build();
				
				Trigger trigger = TriggerBuilder.newTrigger().withIdentity("JobScheduler","JobSchedulerGroup")
						.withSchedule(SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInMinutes(Integer.parseInt(interval)).repeatForever()).build();
				
				scheduler.scheduleJob(job,trigger);
				log.info("Jobs scheduled");
				status = true;
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}

		return status;

	}

	public boolean stop() {
		boolean status = false;
		log.info("Service being stopped");

		try {
			schBean.getScheduler().shutdown();
		} catch (SchedulerException e) {
			log.error("Exception while stopping the service: {}", e.fillInStackTrace());
		}

		return status;

	}
}
