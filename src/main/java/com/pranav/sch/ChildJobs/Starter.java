package com.pranav.sch.ChildJobs;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Starter implements Job {

	private static final Logger log = LoggerFactory.getLogger(Starter.class);
	private static Scheduler sch = null;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		log.info("Inside the starter job");
		
		/*
		if (!isJobRunning(context, context.getJobDetail().getKey().getName(),
				context.getJobDetail().getKey().getGroup())) { 
			*/
		boolean jobRunning = isJobRunning(context, context.getJobDetail().getKey().getName(),context.getJobDetail().getKey().getGroup());
		System.out.println(jobRunning);
		try {
			
			List<String> myList = List.of("Thread1","Thread2");
			log.info("MyList contains:{}", myList.toString());
			
			
			
			for (String val : myList) {
				
				sch = context.getScheduler();
				log.info("Working for this :{}", val);
				
				JobDetail job = JobBuilder.newJob(SyncBehaviour.class).withIdentity("SyncBehaviour","SyncBehaviour" +val).build();
				
				job.getJobDataMap().put("Tname", val);
				
				Trigger trigger = TriggerBuilder.newTrigger().withIdentity("SyncBehaviour","SyncBehaviour" +val)
						.withSchedule(SimpleScheduleBuilder.simpleSchedule()).build();
				
				
				if (sch.checkExists(job.getKey())){
					
					log.info("Deleting the existing job");
					sch.deleteJob(job.getKey());
				}
					sch.scheduleJob(job,trigger);
					log.info("Jobs scheduled");
				
				
				
				
			}
			
			
		} catch (Exception e) {
		log.info("Genaral error before scheduling and all");
		log.info("Exception is:{} ", e);
		
		}
		
		}
	/*else {
			log.info("Job already running");
		}*/
		


	private boolean isJobRunning(JobExecutionContext ctx, String jobName, String groupName) {

		List<JobExecutionContext> jobs;
		try {
			jobs = ctx.getScheduler().getCurrentlyExecutingJobs();
			for (JobExecutionContext job : jobs) {
				if (job.getTrigger().equals(ctx.getTrigger()) && !job.getJobInstance().equals(this)) {
					log.info("There's another instance running, so leaving" + this);

				}
				return true;

			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
}
	/*
	 * private static boolean isJobRunning(JobExecutionContext ctx, String jobName,
	 * String groupName) { boolean status = true;
	 * 
	 * List<JobExecutionContext> currentJobs;
	 * 
	 * try { currentJobs = ctx.getScheduler().getCurrentlyExecutingJobs(); for
	 * (JobExecutionContext jobExecutionContext : currentJobs) {
	 * 
	 * String currentJobName =
	 * jobExecutionContext.getJobDetail().getKey().getName(); String
	 * currentGroupName = jobExecutionContext.getJobDetail().getKey().getGroup();
	 * 
	 * log.info("Job details:: JobName: {}, GroupName: {}", currentJobName,
	 * currentGroupName);
	 * 
	 * if (jobName.equalsIgnoreCase(currentJobName) &&
	 * groupName.equalsIgnoreCase(currentGroupName) &&
	 * !jobExecutionContext.getFireTime().equals(ctx.getFireTime())) { status =
	 * true; log.info("Job already running with jobname: {} and groupName: {}",
	 * currentJobName, currentGroupName); return status; } else { status = false; }
	 * 
	 * }
	 * 
	 * } catch (SchedulerException e) { e.printStackTrace(); }
	 * 
	 * log.info("job status:{}", status);
	 * 
	 * return status;
	 * 
	 * }
	 */


//}
