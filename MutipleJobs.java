package com.pranav.sch.ChildJobs;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.quartz.DateBuilder;
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

public class MutipleJobs implements Job {

	private static final Logger log = LoggerFactory.getLogger(MutipleJobs.class);
	private static Scheduler sch = null;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Inside the starter job");

		List<Long> clientsFromDataSource = List.of(1l, 2l, 3l, 4l);

		// List<Long> clientsFromDataSource = List.<Long>of();
		log.info("MyList contains:{}", clientsFromDataSource.toString());
		if (clientsFromDataSource.isEmpty()) {
			log.info("No Active clients");
		} else {
			int numberOfClientsToProcess = clientsFromDataSource.size();
			if (numberOfClientsToProcess == 1) {
				sch = context.getScheduler();
				JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("SimpleJob", "SimpleJob").build();

				job.getJobDataMap().put("ClientID", clientsFromDataSource.get(0).toString());
				job.getJobDataMap().put("ActiveClientsWithThisJob", Integer.toString(numberOfClientsToProcess));

				Trigger trigger = TriggerBuilder.newTrigger()
						.withIdentity("SimpleTrigger", "SimpleTigger" + clientsFromDataSource.get(0))
						.withSchedule(SimpleScheduleBuilder.simpleSchedule()).build();

				try {
					sch.scheduleJob(job, trigger);
				} catch (SchedulerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				log.info("Jobs scheduled");
			} else {

				for (Long clientData : clientsFromDataSource) {

					sch = context.getScheduler();
					log.info("Working for this :{}", clientData);

					try {
						boolean status = handleJobFrequency(sch, clientData, numberOfClientsToProcess);
					} catch (SchedulerException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}}}		
		}
				
	private boolean handleJobFrequency(Scheduler scheduler, Long clientData, int numberOfClientsToProcess) throws SchedulerException {
		JobBuilder jobBuilder;
		JobDetail jobDetail;
		TriggerBuilder trigBuilder;
		Trigger trigger = null;
		Set<Trigger> triggerList = new HashSet<Trigger>();

		log.info("in createSchedulerTask() for : " + clientData);

		jobBuilder = JobBuilder.newJob(SimpleJob.class);
		jobBuilder = jobBuilder.withIdentity(clientData + "_JOB", "Group" + clientData);
		jobBuilder = jobBuilder.usingJobData("ClientID", clientData.toString());
		jobBuilder = jobBuilder.usingJobData("ActiveClientsWithThisJob", Integer.toString(numberOfClientsToProcess));
		jobDetail = jobBuilder.build();
		log.info("Job created -- [" + jobDetail.getKey() + "]");
		
	//	Date startTime = DateBuilder.nextGivenSecondDate(null, 10);
		
		//for (int i = 0; i < numberOfClientsToProcess; i++) {
			trigBuilder = TriggerBuilder.newTrigger();
			trigBuilder = trigBuilder.withIdentity(clientData + "_TRIGGER_", "Group" + clientData);
			trigBuilder = trigBuilder.startNow();
			trigBuilder = trigBuilder.forJob(jobDetail);
			trigger = trigBuilder.build();
			//triggerList.add(trigger);
		//	Date startTime = DateBuilder.nextGivenMinuteDate(null, 3);
		//}
		
		if (!scheduler.isStarted()) {
            scheduler.start();
     }
		
		System.out.println(triggerList);
		
		if(triggerList.size() > 0){
            scheduler.scheduleJob(jobDetail,triggerList,true);
            for(int j=0;j<scheduler.getTriggersOfJob(jobDetail.getKey()).size();j++){
                log.info((scheduler.getTriggersOfJob(jobDetail.getKey()).get(j)).getNextFireTime()+ "" );
            }
        }else
        {
        	scheduler.scheduleJob(jobDetail,trigger);        	
        }

		return false;
	}

	private Date nowPlusDelay(int i) { // TODO Auto-generated method stub 
		return new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(i));
	}

}
