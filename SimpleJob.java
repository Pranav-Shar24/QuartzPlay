package com.pranav.sch.ChildJobs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleJob implements Job{


	private static final Logger log = LoggerFactory.getLogger(SimpleJob.class);
	//private static Scheduler sch = null;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String clientid = (String) dataMap.get("ClientID");
		String numberOfClientsAssocited = (String) dataMap.get("ActiveClientsWithThisJob");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		String format = dtf.format(now);
		
		log.info("Client iD: {} , Numbers of job associated with this job : {}, Job starting time: {}",clientid,numberOfClientsAssocited,format);
		
		

		
	}
}


