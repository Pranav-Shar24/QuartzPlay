package com.pranav.sch.ChildJobs;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SyncBehaviour implements Job {

	private static final Logger log = LoggerFactory.getLogger(Starter.class);
	private Scheduler sch = null;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		boolean processStatus = false;
		sch = context.getScheduler();
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String value = (String) dataMap.get("Tname");

		if (!isJobRunning(context, context.getJobDetail().getKey().getName(),
				context.getJobDetail().getKey().getGroup())) {
			
			System.out.println("No job same running so processing");
			
			log.info("processing for value: {}", value);
			
			synchronized(SyncBehaviour.class){
				log.info("inside sync block processing starts for :{}",value);
				processStatus = printme(value);
				log.info("inside sync block processing ends for :{}",value);
			}  
			if(processStatus) {
				System.out.println("Do other stuff");
			}else {
				System.out.println("Gracefully exit");
			}
		}else {
			log.info("Simmalr job running");
			
		}

	}

	// creating a helper method to to check which job is running

	private static boolean isJobRunning(JobExecutionContext ctx, String jobName, String groupName) {
		boolean status = true;

		List<JobExecutionContext> currentJobs;

		try {
			currentJobs = ctx.getScheduler().getCurrentlyExecutingJobs();
			for (JobExecutionContext jobExecutionContext : currentJobs) {

				String currentJobName = jobExecutionContext.getJobDetail().getKey().getName();
				String currentGroupName = jobExecutionContext.getJobDetail().getKey().getGroup();

				log.info("Job details:: JobName: {}, GroupName: {}", currentJobName, currentGroupName);

				if (jobName.equalsIgnoreCase(currentJobName) && groupName.equalsIgnoreCase(currentGroupName)
						&& !jobExecutionContext.getFireTime().equals(ctx.getFireTime())) {
					status = true;
					log.info("Job already running with jobname: {} and groupName: {}", currentJobName,
							currentGroupName);
					return status;
				} else {
					status = false;
				}

			}
			log.info("job status:{}", status);

		} catch (SchedulerException e) {
			log.info("Error:{}",e);
			
		}
		
		
		
		return status;

	}

	// verify method

	private static boolean printme(String tName) {
		boolean status = false;
		for (int i = 0; i < 100; i++) {
			try {
				Thread.sleep(1000);
				System.out.println("processing for "+tName);
				log.info("Count: {} for teh process name:{}", i, tName );
			} catch (InterruptedException e) {
				status = false;
				e.printStackTrace();
			}
			status = true;
		}
		return status;

	}

}
